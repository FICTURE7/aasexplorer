package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.concurrent.Executor;

/**
 * Represents a getSaver which saves {@link Source} instances.
 *
 * @author FICTURE7
 */
public interface Saver {

    /**
     * Returns the {@link Executor} which will be used for async operations.
     *
     * @return {@link Executor} which will be used for async operations.
     */
    CallbackExecutor getExecutor();

    <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception;

    //TODO: Further clean up parameters to use (Class<? extends Examination> examination, int subjectId, Iterable<ResourceSource> resourceSources).

    void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception;
}
