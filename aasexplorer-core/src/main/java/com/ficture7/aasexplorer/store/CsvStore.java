package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sun.security.util.Length;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a {@link Store} which stores data in .csv format.
 *
 * @author FICTURE7
 */
public class CsvStore extends Store {

    //TODO: Version handling?

    // Root root of the files.
    private File root;
    // File we're going to read and write subject sources to.
    private File subjectsFile;
    // File we're going to read and write resource sources to.
    private File resourcesFile;

    /**
     * Constructs a new instance of the {@link CsvStore} class with the specified {@link Explorer}.
     *
     * @param explorer {@link Explorer} instance.
     * @throws IllegalArgumentException {@code getExplorer} is null.
     */
    public CsvStore(Explorer explorer) {
        super(explorer);

        configure("default");
    }

    /**
     * Returns root directory of the {@link CsvStore}.
     *
     * @return Root directory of the {@link CsvStore}.
     */
    public File root() {
        return root;
    }

    /**
     * Configures the {@link CsvStore} instance with the specified root root.
     *
     * @param directory 'Root' root.
     * @throws IllegalArgumentException {@code root} is null.
     */
    public void configure(String directory) {
        checkNotNull(directory, "root");
        this.root = new File(directory);

        subjectsFile = new File(directory + "-subjects.csv");
        resourcesFile = new File(directory + "-resources.csv");
    }

    //TODO: Optimize by doing manual reading and stuff.

    @Override
    public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {
        FileWriter fileWriter = new FileWriter(subjectsFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            for (SubjectSource source : subjectSources) {
                bufferedWriter.append(examinationClass.getName()).append(',');
                bufferedWriter.append(source.client().getClass().getName()).append(',');
                bufferedWriter.append(String.valueOf(source.id())).append(',');
                bufferedWriter.append(source.name()).append(',');
                bufferedWriter.append(String.valueOf(source.date().getTime())).append(',');
                bufferedWriter.append(source.uri().toString()).append("\r\n");
            }
        } finally {
            bufferedWriter.close();
        }
    }

    @Override
    public <T extends Examination> void saveResources(Class<T> examinationClass, Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {
        FileWriter fileWriter = new FileWriter(resourcesFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            for (ResourceSource source : resourceSources) {
                bufferedWriter.append(examinationClass.getName()).append(',');
                bufferedWriter.append(source.client().getClass().getName()).append(',');
                bufferedWriter.append(String.valueOf(subject.id())).append(',');
                bufferedWriter.append(source.name()).append(',');
                bufferedWriter.append(String.valueOf(source.date().getTime())).append(',');
                bufferedWriter.append(source.uri().toString()).append("\r\n");
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
        if (!subjectsFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(subjectsFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        CsvReader csvReader = new CsvReader(bufferedReader);

        // List of sources for the examination type.
        List<SubjectSource> sources = new ArrayList<>();

        // Class name of the Examination specified.
        String queriedExamination = examinationClass.getName();

        try {
            while (csvReader.nextRow()) {
                String examination = csvReader.nextAsString();
                // Exit early if the examination of the source is not the same
                // as the queried one.
                if (!examination.contentEquals(queriedExamination)) {
                    continue;
                }

                Client client = getClient(csvReader.nextAsString());
                // Exit early if the Explorer which owns this Store does not have
                // a Client instance of the type of the source.
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

        /* Return null if the sources list was empty. */
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
        if (!resourcesFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(resourcesFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        CsvReader csvReader = new CsvReader(bufferedReader);

        // List of resource source for the subject.
        List<ResourceSource> sources = new ArrayList<>();

        // Class name of the Examination specified.
        String queriedExamination = subject.examination().getClass().getName();

        try {
            while (csvReader.nextRow()) {
                int id = csvReader.nextAsInt();
                // Exit early if the subject ID differs.
                if (id != subject.id()) {
                    continue;
                }

                String examination = csvReader.nextAsString();
                // Exit early if the examination of the source is not the same
                // as the queried one.
                if (!examination.contentEquals(queriedExamination)) {
                    continue;
                }

                Client client = getClient(csvReader.nextAsString());
                // Exit early if the Explorer which owns this Store does not have
                // a Client instance of the type of the source.
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

        /* Return null if the sources list was empty. */
        if (sources.size() == 0) {
            return null;
        }

        return sources;
    }

    /* Provides methods to read .csv files.
     *
     * NOTE: It does not support quoted strings and does not
     * necessarily use '\r\n' line endings. (Uses BufferedReader.readLine())
     */
    private static final class CsvReader {

        private final BufferedReader reader;

        private String row;
        private int rowLength;
        private int start;
        private int end;

        public CsvReader(BufferedReader reader) throws IOException {
            this.reader = checkNotNull(reader, "reader");
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
            while (row.charAt(end) != ',' && ++end < rowLength);
        }

        /* Returns the current value as a string. */
        private String asString() {
            return row.substring(start, end);
        }

        /* Returns the current value as an integer. */
        private int asInt() {
            return Integer.parseInt(asString());
        }
    }
}
