package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

public class MockStore extends Store {

    public MockStore(Explorer explorer) {
        super(explorer);
    }

    @Override
    public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {

    }

    @Override
    public <T extends Examination> void saveResources(Class<T> examinationClass, Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {

    }

    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        return null;
    }

    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        return null;
    }
}
