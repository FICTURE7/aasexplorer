package com.ficture7.aasexplorer.client

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.net.URI

/**
 * Represents a [Client] which uses HTTP as transport.
 *
 * @author FICTURE7
 */
abstract class HttpClient : Client {

    /**
     * Returns the HTML page at the specified [URI] in the form of a [Document].
     *
     * @param uri [URI] of the HTML page.
     * @return HTML page at the specified [URI] in the form of a [Document].
     * @throws NullPointerException `getURI` is null.
     * @throws DownloadException    Exception caught when downloading the HTML page.
     */
    @Throws(DownloadException::class)
    protected operator fun get(uri: URI): Document {
        val uriString = uri.toString()
        try {
            return Jsoup.connect(uriString)
                    .timeout(TIMEOUT)
                    .userAgent(USER_AGENT)
                    .get()
        } catch (e: Exception) {
            throw DownloadException(uriString, e)
        }
    }

    /**
     * Parses the specified [Document] instance and uses the specified [Processor]
     * to process parsed data from the [Document] instance.
     *
     * @param document [Document] instance.
     * @param processor [Processor] instance.
     * @throws ParseException Exception caught when parsing the [Document].
     */
    @Throws(ParseException::class)
    protected abstract fun parse(document: Document, processor: Processor)

    /**
     * Represents a [Document] processor.
     *
     * @author FICTURE7
     */
    protected abstract class Processor {
        /**
         * Processes the specified getName and URL.
         * @param name Name.
         * @param url URL.
         */
        abstract fun process(name: String, url: String)
    }

    companion object {
        /**
         * User-Agent used in the headers of requests made by [.get].
         */
        protected val USER_AGENT = "aasexplorer-core-" + HttpClient::class.java.`package`.implementationVersion

        /**
         * Timeout used for the requests made by [.get],
         */
        protected val TIMEOUT = 5000
    }
}
