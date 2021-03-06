package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class OLevelExaminationTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_loader__throwsException() {
        new OLevelExamination(null, mock(Saver.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_saver__throwsException() {
        new OLevelExamination(mock(Loader.class), null);
    }

    @Test
    public void name__returnsNonNull() {
        Examination examination = new OLevelExamination(mock(Loader.class), mock(Saver.class));
        assertNotNull(examination.getSubjects());
    }
}