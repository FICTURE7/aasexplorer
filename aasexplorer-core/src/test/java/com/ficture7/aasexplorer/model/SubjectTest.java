package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class SubjectTest {

    private Examination examination;
    private Loader loader;
    private Saver saver;

    @Before
    public void setUp() {
        examination = mock(Examination.class);
        loader = mock(Loader.class);
        saver = mock(Saver.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor_nameNull_exception() {
        new Subject(examination, loader, saver, 0, null);
    }

    @Test
    public void id_returnsInitiated_id() {
        Subject subject = new Subject(examination, loader, saver, 1, "a'");
        assertEquals(1, subject.id());
    }

    @Test
    public void name_returnsInitiated_name() {
        Subject subject = new Subject(examination, loader, saver, 1, "a");
        assertEquals("a", subject.name());
    }

    @Test
    public void sources_returnsNonNull() {
        Subject subject = new Subject(examination, loader, saver, 1, "a");
        assertNotNull(subject.sources());
    }

    @Test
    public void resources_returnsNonNull() {
        Subject subject = new Subject(examination, loader, saver, 1, "a");
        assertNotNull(subject.resources());
    }

    @Test
    public void hashCode_returnsId() {
        Subject subject = new Subject(examination, loader, saver, 1, "a");

        assertEquals(1, subject.hashCode());
        assertEquals(subject.id(), subject.hashCode());
    }

    @Test
    public void toString_returnsFormattedName() {
        Subject subject = new Subject(examination, loader, saver, 1000, "test");

        assertEquals("test (1000)", subject.toString());
    }
}