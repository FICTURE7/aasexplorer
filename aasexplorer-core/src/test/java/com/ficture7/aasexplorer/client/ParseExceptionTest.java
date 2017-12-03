package com.ficture7.aasexplorer.client;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ParseExceptionTest {

    @Test
    public void ctor__causeSet() {
        Throwable cause = new Exception();
        ParseException exception = new ParseException(cause);

        assertSame(cause, exception.getCause());
    }
}