package com.ficture7.aasexplorer.client;

/**
 * Exception thrown when there was an issue parsing a document.
 *
 * @author FICTURE7
 */
public class ParseException extends Exception {

    /**
     * Constructs a new instance of the {@link ParseException} class.
     *
     * @param cause Cause.
     */
    ParseException(Throwable cause) {
        super(cause);
    }
}
