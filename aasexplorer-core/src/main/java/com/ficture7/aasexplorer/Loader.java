package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Source;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

/**
 * Represents a loader which loads {@link Source} instances.
 *
 * @author FICTURE7
 */
public interface Loader {

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T> Type of {@link Examination}.
     * @return Loaded {@link SubjectSource}; can return null under certain circumstances.
     * @throws Exception Exception raised when loading the {@link SubjectSource}s.
     */
    <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception;

    //TODO: Simplify the loadResources parameters since subject already has the examinationClass.

    <T extends Examination> Iterable<ResourceSource> loadResources(Class<T> examinationClass, Subject subject) throws Exception;
}
