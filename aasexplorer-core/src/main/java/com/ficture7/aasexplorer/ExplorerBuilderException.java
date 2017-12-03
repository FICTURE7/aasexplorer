package com.ficture7.aasexplorer;

/**
 * Exception thrown when there is an issue when using the {@link ExplorerBuilder} class.
 *
 * @author FICTURE7
 */
public class ExplorerBuilderException extends Exception {

    /**
     * Constructs a new instance of the {@link ExplorerBuilderException} class with the specified
     * message.
     *
     * @param message Message describing the error.
     */
    ExplorerBuilderException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of the {@link ExplorerBuilderException} class with the specified
     * cause.
     *
     * @param cause Cause of the error.
     */
    ExplorerBuilderException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new instance of the {@link ExplorerBuilderException} class with the specified
     * message and cause.
     *
     * @param message Message describing the error.
     * @param cause Cause of the error.
     */
    ExplorerBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
