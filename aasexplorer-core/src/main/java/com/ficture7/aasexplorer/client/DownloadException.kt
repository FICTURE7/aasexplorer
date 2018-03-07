package com.ficture7.aasexplorer.client

/**
 * Exception thrown when there was an issue downloading a document.
 *
 * @author FICTURE7
 */
class DownloadException : Exception {

    /**
     * Constructs a new instance of the [DownloadException] class.
     *
     * @param urlString URL.
     * @param cause Cause.
     */
    internal constructor(urlString: String?, cause: Throwable?) : super(
            if (urlString == null)
                "Failed to download a resource."
            else
                String.format("Failed to download a resource at '%s'.", urlString),
            cause
    )
}
