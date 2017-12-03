package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import java.net.URI;
import java.util.Date;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a {@link Resource} source.
 *
 * @author FICTURE7
 */
public class ResourceSource implements Source {

    private final Client client;
    private final String name;
    private final Date date;
    private final URI uri;

    /**
     * Constructs a new instance of the {@link ResourceSource} class with the specified
     * {@link Client} instance which acquired the {@link ResourceSource}, resource name,
     * {@link Date} when the source was acquired and {@link URI} pointing to the source.
     *
     * @param client {@link Client} instance which acquired the {@link ResourceSource}.
     * @param name   Resource name.
     * @param date   {@link Date} when the source was acquired.
     * @param uri    {@link URI} pointing to the source.
     * @throws NullPointerException {@code client} is null.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code date} is null.
     * @throws NullPointerException {@code uri} is null.
     */
    public ResourceSource(Client client, String name, Date date, URI uri) {
        this.client = checkNotNull(client, "client");
        this.name = checkNotNull(name, "name");
        this.date = checkNotNull(date, "date");
        this.uri = checkNotNull(uri, "uri");
    }

    /**
     * Returns the {@link Client} instance which acquired the {@link ResourceSource}.
     *
     * @return {@link Client} instance which acquired the {@link ResourceSource}.
     */
    @Override
    public Client client() {
        return client;
    }

    /**
     * Returns the name of the {@link ResourceSource}.
     *
     * @return Name of the {@link ResourceSource}
     */
    public String name() {
        return name;
    }

    /**
     * Returns the {@link Date} when the source was acquired.
     *
     * @return {@link Date} when the source was acquired.
     */
    @Override
    public Date date() {
        return date;
    }

    /**
     * Returns the {@link URI} pointing to the source.
     *
     * @return {@link URI} pointing to the source.
     */
    @Override
    public URI uri() {
        return uri;
    }
}
