package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Source;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

/**
 * Represents a saver which saves {@link Source} instances.
 *
 * @author FICTURE7
 */
public interface Saver {

    <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception;

    void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception;
}
