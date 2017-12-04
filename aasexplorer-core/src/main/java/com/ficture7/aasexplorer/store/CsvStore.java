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
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a {@link Store} which stores data in .csv format.
 *
 * @author FICTURE7
 */
public class CsvStore extends Store {

    //TODO: Version handling?

    // 'Root' name of the files.
    private String name;
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
     * Returns the 'root' name of the {@link CsvStore} file.
     *
     * @return 'Root' name of the {@link CsvStore} file.
     */
    public String name() {
        return name;
    }

    /**
     * Configures the {@link CsvStore} instance with the specified 'root' name.
     *
     * @param name 'Root' name.
     * @throws IllegalArgumentException {@code name} is null.
     */
    public void configure(String name) {
        this.name = ObjectUtil.checkNotNull(name, "name");

        subjectsFile = new File(name + "-subjects.csv");
        resourcesFile = new File(name + "-resources.csv");
    }

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

    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        if (!subjectsFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(subjectsFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // List of sources for the examination type.
        List<SubjectSource> sources = new ArrayList<>();

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");

                //TODO: Optimize by doing manual reading and stuff.

                String examination = columns[0];
                Client client = getClient(columns[1]);
                int id = Integer.parseInt(columns[2]);
                String name = columns[3];
                Date date = new Date(Long.parseLong(columns[4]));
                URI uri = URI.create(columns[5]);

                if (examinationClass.getName().equals(examination)) {
                    SubjectSource source = new SubjectSource(client, id, name, date, uri);
                    sources.add(source);
                }
            }
        } finally {
            bufferedReader.close();
        }

        return sources;
    }

    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        if (!resourcesFile.exists()) {
            return null;
        }

        List<ResourceSource> sources = new ArrayList<>();
        FileReader fileReader = new FileReader(resourcesFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");

                //TODO: Optimize by doing manual reading and stuff.

                String examination = columns[0];
                Client client = getClient(columns[1]);
                int id = Integer.parseInt(columns[2]);
                String name = columns[3];
                Date date = new Date(Long.parseLong(columns[4]));
                URI uri = URI.create(columns[5]);

                if (subject.examination().getClass().getName().equals(examination) && id == subject.id()) {
                    ResourceSource source = new ResourceSource(client, name, date, uri);
                    sources.add(source);
                }
            }
        } finally {
            bufferedReader.close();
        }

        if (sources.size() == 0) {
            return null;
        }

        return sources;
    }
}
