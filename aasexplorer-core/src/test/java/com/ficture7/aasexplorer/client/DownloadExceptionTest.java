package com.ficture7.aasexplorer.client;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DownloadExceptionTest {

    @Test
    public void ctor__urlString_null__errorMessage_still_set() {
        DownloadException exception = new DownloadException(null, null);
        assertNotNull(exception.getMessage());
    }

    @Test
    public void ctor__message_and_cause_set() {
        Throwable cause = new Exception();
        String urlString = "http://test.com/";

        DownloadException exception = new DownloadException(urlString, cause);

        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().contains(urlString));
        assertSame(cause, exception.getCause());
    }
}