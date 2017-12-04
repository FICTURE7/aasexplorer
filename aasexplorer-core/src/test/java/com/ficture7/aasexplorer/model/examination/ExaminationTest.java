package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ExaminationTest {

    @Test(expected = NullPointerException.class)
    public void ctor__null_loader__throwsException() {
        new Examination(null, mock(Saver.class)) {
            @Override
            public String name() {
                return null;
            }
        };
    }

    @Test(expected = NullPointerException.class)
    public void ctor__null_saver__throwsException() {
        new Examination(mock(Loader.class), null) {
            @Override
            public String name() {
                return null;
            }
        };
    }

    @Test
    public void subjects__returnsNonNull() {
        Examination examination = new Examination(mock(Loader.class), mock(Saver.class)) {
            @Override
            public String name() {
                return null;
            }
        };

        assertNotNull(examination.subjects());
    }
}