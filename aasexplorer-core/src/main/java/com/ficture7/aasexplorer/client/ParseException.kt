package com.ficture7.aasexplorer.client

/**
 * Exception thrown when there was an issue parsing a document.
 *
 * @author FICTURE7
 */
class ParseException : Exception {

    /**
     * Constructs a new instance of the [ParseException] class.
     *
     * @param cause Cause.
     */
    internal constructor(cause: Throwable) : super(cause)
}
