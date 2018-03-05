package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class Subject_FactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_examination__throwsException() {
        new Subject.Factory(null, mock(Loader.class), mock(Saver.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_loader__throwsException() {
        new Subject.Factory(mock(Examination.class), null, mock(Saver.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_saver__throwsException() {
        new Subject.Factory(mock(Examination.class), mock(Loader.class), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create__null_name__throwsException() {
        Subject.Factory factory = new Subject.Factory(mock(Examination.class), mock(Loader.class), mock(Saver.class));
        factory.create(10, null);
    }

    @Test
    public void create__instance_created() {
        Subject.Factory factory = new Subject.Factory(mock(Examination.class), mock(Loader.class), mock(Saver.class));

        Subject subject = factory.create(10, "oi");
        assertNotNull(subject);
    }
}