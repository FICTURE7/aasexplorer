package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a {@link Store} which stores data in .csv format.
 *
 * @author FICTURE7
 */
public class CsvStore extends Store {

    //TODO: Version handling?

    // Root directory of the files.
    private File rootDir;

    // Map mapping Class<? extends Examination> to their respective directory.
    private final Map<Class<? extends Examination>, File> examinationsDirs;
    // Determine if the Store has been configured.
    private boolean configured;

    /**
     * Constructs a new instance of the {@link CsvStore} class with the specified {@link Explorer}.
     *
     * @param explorer {@link Explorer} instance.
     */
    public CsvStore(@NotNull Explorer explorer) {
        super(explorer);

        examinationsDirs = new HashMap<>();
    }

    /**
     * Returns root directory of the {@link CsvStore}.
     *
     * @return Root directory of the {@link CsvStore}.
     */
    public File getRoot() {
        return rootDir;
    }

    /**
     * Configures the {@link CsvStore} instance with the specified root directory.
     *
     * @param directoryPath Root directory.
     * @throws IllegalArgumentException {@code directoryPath} is not a directory.
     */
    public void configure(@NotNull String directoryPath) {
        if (configured) {
            throw new IllegalStateException("CsvStore instance was already configured.");
        }

        rootDir = new File(directoryPath);

        /*
            Checks if the specified path exists and is a directory.
            If it does not exists, we create it. If its not a directory we
            throw an Exception.
         */
        if (rootDir.exists()) {
            if (!rootDir.isDirectory()) {
                throw new IllegalArgumentException("Specified path: " + directoryPath + ", is not a directory");
            }

            File[] files = rootDir.listFiles();
            if (files == null) {
                return;
            }

            /*
                Iterate through the sub directories of the root directory
                to look for examination directories.
             */
            for (File file : files) {
                if (file.isDirectory()) {
                    String examinationClassName = file.getName();
                    Class<?> examinationClass;

                    /*
                        Try to get the Class<? extends Examination> instance from the
                        getName of the class.
                     */
                    try {
                        examinationClass = Class.forName(examinationClassName);
                        // Make sure its an Examination class & castable.
                        if (examinationClass.getSuperclass() != Examination.class) {
                            continue;
                        }
                    } catch (ClassNotFoundException e) {
                        // We move on with life if a Class with the directory getName does not exists.
                        continue;
                    }

                    // Map the Class instance to the directory.
                    examinationsDirs.put((Class<? extends Examination>) examinationClass, file);
                }
            }
        } else {
            if (!rootDir.mkdir()) {
                throw new IllegalStateException("Unable to create directory.");
            }
        }

        configured = true;
    }

    @Override
    public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {
        if (!configured) {
            throw new IllegalStateException("CsvStore has not been configured.");
        }

        File examinationDir = examinationsDirs.get(examinationClass);
        if (examinationDir == null) {
            examinationDir = new File(rootDir, examinationClass.getName());
            examinationDir.mkdir();

            examinationsDirs.put(examinationClass, examinationDir);
        }

        File subjectsFile = new File(examinationDir, "subjects.csv");

        FileWriter fileWriter = new FileWriter(subjectsFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        CsvWriter csvWriter = new CsvWriter(bufferedWriter);

        try {
            for (SubjectSource source : subjectSources) {
                csvWriter.writeNext(source.getClient().getClass().getName());
                csvWriter.writeNext(source.getId());
                csvWriter.writeNext(source.getName());
                csvWriter.writeNext(source.getDate());
                csvWriter.writeNext(source.getURI());
                csvWriter.writeNext();
            }
        } finally {
            bufferedWriter.close();
        }
    }

    @Override
    public void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {
        if (!configured) {
            throw new IllegalStateException("CsvStore has not been configured.");
        }

        Class<? extends Examination> examinationClass = subject.getExamination().getClass();

        File examinationDir = examinationsDirs.get(examinationClass);
        if (examinationDir == null) {
            examinationDir = new File(rootDir, examinationClass.getName());
            examinationDir.mkdir();

            examinationsDirs.put(examinationClass, examinationDir);
        }

        File resourcesDir = new File(examinationDir, "resources");
        if (!resourcesDir.exists()) {
            resourcesDir.mkdir();
        }

        File resourcesFile = new File(resourcesDir, subject.getId() + ".csv");
        FileWriter fileWriter = new FileWriter(resourcesFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        CsvWriter csvWriter = new CsvWriter(bufferedWriter);

        try {
            for (ResourceSource source : resourceSources) {
                csvWriter.writeNext(source.getClient().getClass().getName());
                csvWriter.writeNext(source.getName());
                csvWriter.writeNext(source.getDate());
                csvWriter.writeNext(source.getURI());
                csvWriter.writeNext();
            }
        } finally {
            bufferedWriter.close();
        }
    }

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T>              Type of {@link Examination}.
     * @return {@link SubjectSource}s; can return null under certain circumstances.
     * @throws Exception Exception when loading the {@link SubjectSource}s.
     */
    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        if (!configured) {
            throw new IllegalStateException("CsvStore has not been configured.");
        }

        /*
            Get the directory of the examination from the Class<? extends Examination> -> File map.
            If it does not exists in the map, we exit early and return null.
         */
        File examinationDir = examinationsDirs.get(examinationClass);
        if (examinationDir == null) {
            return null;
        }

        /*
            Directory structure is something like this:

            /{root}/{full identified of examination class}/subjects.csv
            -->->
            /{rootDir}/{examinationDir}/subjects.csv

            Get the file which contains the subjects of examination.
            Exit early and return null, if the file does not exists.
         */
        File subjectsFile = new File(examinationDir, "subjects.csv");
        if (!subjectsFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(subjectsFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        CsvReader csvReader = new CsvReader(bufferedReader);

        // List of getSources for the examination type.
        List<SubjectSource> sources = new ArrayList<>();

        try {
            while (csvReader.nextRow()) {
                Client client = getClient(csvReader.nextAsString());
                /*
                    Exit early if the Explorer which owns this Store does not have
                    a Client instance of the type of the source.
                 */
                if (client == null) {
                    continue;
                }

                int id = csvReader.nextAsInt();
                String name = csvReader.nextAsString();
                Date date = csvReader.nextAsDate();
                URI uri = csvReader.nextAsURI();

                SubjectSource source = new SubjectSource(client, id, name, date, uri);
                sources.add(source);
            }
        } finally {
            bufferedReader.close();
        }

        // Return null if the getSources list was empty.
        if (sources.size() == 0) {
            return null;
        }

        return sources;
    }

    /**
     * Loads the {@link ResourceSource}s for the specified {@link Subject} instance.
     *
     * @param subject {@link Subject} instance.
     * @return {@link ResourceSource}s; can return null under certain circumstances.
     * @throws Exception Exception  when loading the {@link ResourceSource}s.
     */
    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        if (!configured) {
            throw new IllegalStateException("CsvStore has not been configured.");
        }

        Class<? extends Examination> examinationClass = subject.getExamination().getClass();

        /*
            Get the directory of the examination from the Class<? extends Examination> -> File map.
            If it does not exists in the map, we exit early and return null.
         */
        File examinationDir = examinationsDirs.get(examinationClass);
        if (examinationDir == null) {
            return null;
        }

        /*
            Directory structure is something like this:

            /{root}/{full identifier of examination class}/resources/
            -->->
            /{rootDir}/{examinationDir}/resources/

            Get the directory which contains the resources of the subjects.
            Exit early if it does not exists and return null.
         */
        File resourcesDir = new File(examinationDir, "resources");
        if (!resourcesDir.exists()) {
            return null;
        }

        /*
            Get the .csv file which contains the resource getSources.
            Exit early if it does exists and return null.
         */
        File resourcesFile = new File(resourcesDir, subject.getId() + ".csv");
        if (!resourcesFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(resourcesFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        CsvReader csvReader = new CsvReader(bufferedReader);

        // List of resource source for the subject.
        List<ResourceSource> sources = new ArrayList<>();

        try {
            while (csvReader.nextRow()) {
                Client client = getClient(csvReader.nextAsString());
                /*
                    Exit early if the Explorer which owns this Store does not have
                    a Client instance of the type of the source.
                 */
                if (client == null) {
                    continue;
                }

                String name = csvReader.nextAsString();
                Date date = csvReader.nextAsDate();
                URI uri = csvReader.nextAsURI();

                ResourceSource source = new ResourceSource(client, name, date, uri);
                sources.add(source);
            }
        } finally {
            bufferedReader.close();
        }

        // Return null if the getSources list was empty.
        if (sources.size() == 0) {
            return null;
        }

        return sources;
    }

    /*
        Provides methods to read .csv files.

        NOTE: It does not support quoted strings and does not
        necessarily use '\r\n' line endings. (Uses BufferedReader.readLine())
     */
    private static final class CsvReader {

        private final BufferedReader reader;

        private String row;
        private int rowLength;
        private int start;
        private int end;

        public CsvReader(@NotNull BufferedReader reader) throws IOException {
            this.reader = reader;
        }

        public boolean nextRow() throws IOException {
            // Read the next line from the BufferedReader.
            row = reader.readLine();
            if (row == null) {
                // Returns false if end of file.
                return false;
            }

            rowLength = row.length();
            // Reset pointers & return true to indicate there is potentially more to read.
            start = -1;
            end = -1;
            return true;
        }

        public String nextAsString() throws IOException {
            next();
            return asString();
        }

        public int nextAsInt() throws IOException {
            next();
            return asInt();
        }

        public Date nextAsDate() throws IOException {
            long timestamp = Long.parseLong(nextAsString());
            return new Date(timestamp);
        }

        public URI nextAsURI() throws IOException {
            String uriString = nextAsString();
            return URI.create(uriString);
        }

        /* Looks for the start index and end index of the next value in the row. */
        private void next() throws IOException {
            // Check if we got a row first.
            if (row == null) {
                throw new IOException("End of row.");
            } else {
                start = end + 1;
                end = start;
            }

            /* Iterate through the characters until we hit a ',' or the end of the line. */
            while (row.charAt(end) != ',' && ++end < rowLength) ;
        }

        /* Returns the current value/cell as a string. */
        private String asString() {
            return row.substring(start, end);
        }

        /* Returns the current value/cell as an integer. */
        private int asInt() {
            return Integer.parseInt(asString());
        }
    }

    /*
        Provides methods to write .csv files.
     */
    private static final class CsvWriter {

        // Tracks if the writer is at the start of a row.
        private boolean start;
        private final BufferedWriter writer;

        public CsvWriter(@NotNull BufferedWriter writer) {
            this.writer = writer;
            start = true;
        }

        /* Starts writing the next row. */
        public void writeNext() throws IOException {
            writer.write("\r\n");
            start = true;
        }

        public void writeNext(String value) throws IOException {
            if (!start) {
                writer.write(',');
            }
            writer.write(value);
            start = false;
        }

        public void writeNext(int value) throws IOException {
            writeNext(String.valueOf(value));
        }

        public void writeNext(Date date) throws IOException {
            long timestamp = date.getTime();
            writeNext(String.valueOf(timestamp));
        }

        public void writeNext(URI uri) throws IOException {
            String uriString = uri.toString();
            writeNext(uriString);
        }
    }
}
