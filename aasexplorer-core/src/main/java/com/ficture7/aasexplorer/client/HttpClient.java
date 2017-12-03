package com.ficture7.aasexplorer.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a {@link Client} which uses HTTP as transport.
 *
 * @author FICTURE7
 */
public abstract class HttpClient implements Client {

    /**
     * User-Agent used in the headers of requests made by {@link #get(URI)}.
     */
    protected static final String USER_AGENT = "aasexplorer-core-" + HttpClient.class.getPackage().getImplementationVersion();

    /**
     * Timeout used for the requests made by {@link #get(URI)},
     */
    protected static final int TIMEOUT = 5000;

    /**
     * Returns the HTML page at the specified {@link URI} in the form of a {@link Document}.
     *
     * @param uri {@link URI} of the HTML page.
     * @return HTML page at the specified {@link URI} in the form of a {@link Document}.
     * @throws NullPointerException {@code uri} is null.
     * @throws DownloadException    Exception caught when downloading the HTML page.
     */
    protected Document get(URI uri) throws DownloadException {
        checkNotNull(uri, "uri");

        String uriString = uri.toString();
        try {
            return Jsoup.connect(uriString)
                        .timeout(TIMEOUT)
                        .userAgent(USER_AGENT)
                        .get();
        } catch (Exception e) {
            throw new DownloadException(uriString, e);
        }
    }

    /**
     * Parses the specified {@link Document} instance and uses the specified {@link Processor}
     * to process parsed data from the {@link Document} instance.
     *
     * @param document {@link Document} instance.
     * @param processor {@link Processor} instance.
     * @throws ParseException Exception caught when parsing the {@link Document}.
     */
    protected abstract void parse(Document document, Processor processor) throws ParseException;

    /**
     * Represents a {@link Document} processor.
     *
     * @author FICTURE7
     */
    protected static abstract class Processor {

        /**
         * Processes the specified name and URL.
         * @param name Name.
         * @param url URL.
         */
        public abstract void process(String name, String url);
    }
}
