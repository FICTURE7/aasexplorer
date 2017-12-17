package com.ficture7.aasexplorer.client;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

/**
 * Represents a client which retrieves {@link SubjectSource} and {@link ResourceSource} from a network (or somewhere).
 *
 * @author FICTURE7.
 */
public interface Client {

    /**
     * Returns the name of the {@link Client}.
     *
     * @return Name of the {@link Client}.
     */
    String name();

    /**
     * Retrieves the {@link SubjectSource}s of the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T>              Type of {@link Examination}.
     * @return {@link SubjectSource}s of the specified {@link Examination} type; returns null if the specified examination is not supported.
     * @throws ParseException    Exception when parsing the data.
     * @throws DownloadException Exception when downloading the data.
     */
    <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException;

    /**
     * Retrieves the {@link ResourceSource}s of the specified {@link SubjectSource}.
     *
     * @param subjectSource {@link SubjectSource} instance.
     * @return {@link ResourceSource}s of the specified {@link SubjectSource}.
     * @throws ParseException    Exception when parsing the data.
     * @throws DownloadException Exception when downloading the data.
     */
    Iterable<ResourceSource> getResources(SubjectSource subjectSource) throws ParseException, DownloadException;
}
