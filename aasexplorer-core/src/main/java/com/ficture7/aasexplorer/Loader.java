package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.concurrent.Executor;

/**
 * Represents a getLoader which loads {@link Source} instances.
 *
 * @author FICTURE7
 */
public interface Loader {

    /**
     * Returns the {@link Executor} which will be used for async operations.
     * @return {@link Executor} which will be used for async operations.
     */
    CallbackExecutor getExecutor();

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T> Type of {@link Examination}.
     * @return {@link SubjectSource}s; can return null under certain circumstances.
     * @throws Exception Exception when loading the {@link SubjectSource}s.
     */
    <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception;

    //TODO: Further clean up parameters to use (Class<? extends Examination> examination, int subjectId)

    /**
     * Loads the {@link ResourceSource}s for the specified {@link Subject} instance.
     *
     * @param subject {@link Subject} instance.
     * @return {@link ResourceSource}s; can return null under certain circumstances.
     * @throws Exception Exception  when loading the {@link ResourceSource}s.
     */
    Iterable<ResourceSource> loadResources(Subject subject) throws Exception;
}
