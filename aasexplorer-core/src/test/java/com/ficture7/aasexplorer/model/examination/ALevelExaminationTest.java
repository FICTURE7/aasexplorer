package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ALevelExaminationTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_loader__throwsException() {
        new ALevelExamination(null, mock(Saver.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_saver__throwsException() {
        new ALevelExamination(mock(Loader.class), null);
    }

    @Test
    public void name__returnsNonNull() {
        Examination examination = new ALevelExamination(mock(Loader.class), mock(Saver.class));
        assertNotNull(examination.getName());
    }
}