package com.ficture7.aasexplorer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ExplorerBuilderExceptionTest {

    // MAXIMUUM COVEROGEE
    @Test
    public void ctor__message_set() {
        ExplorerBuilderException exception = new ExplorerBuilderException("A");
        assertEquals("A", exception.getMessage());
    }

    @Test
    public void ctor__message_and_cause_set() {
        Exception cause = new Exception("B");
        ExplorerBuilderException exception = new ExplorerBuilderException("A", cause);

        assertEquals("A", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    public void ctor__cause_set() {
        Exception cause = new Exception("B");
        ExplorerBuilderException exception = new ExplorerBuilderException("A", cause);

        assertSame(cause, exception.getCause());
    }
}