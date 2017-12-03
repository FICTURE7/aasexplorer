package com.ficture7.aasexplorer.client;

/**
 * Exception thrown when there was an issue downloading a document.
 *
 * @author FICTURE7
 */
public class DownloadException extends Exception {

    /**
     * Constructs a new instance of the {@link DownloadException} class.
     *
     * @param urlString URL.
     * @param cause Cause.
     */
    DownloadException(String urlString, Throwable cause) {
        super(urlString == null ? "Failed to download a resource." : String.format("Failed to download a resource at '%s'.", urlString),
                cause
        );
    }
}
