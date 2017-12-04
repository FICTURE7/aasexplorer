package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import java.net.URI;
import java.util.Date;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a {@link Subject} source.
 *
 * @author FICTURE7
 */
public class SubjectSource implements Source {

    private final Client client;
    private final int id;
    private final String name;
    private final URI uri;
    private final Date date;

    /**
     * Constructs a new instance of the {@link SubjectSource} class with the specified
     * {@link Client} instance which acquired the {@link SubjectSource}, subject ID/code,
     * subject name, {@link Date} when the source was acquired and {@link URI} pointing to the source.
     *
     * @param client {@link Client} instance which acquired the {@link SubjectSource}.
     * @param id     Subject ID/code.
     * @param name   Subject name.
     * @param date   {@link Date} when the source was acquired.
     * @param uri    {@link URI} pointing to the source.
     * @throws NullPointerException {@code client} is null.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code uri} is null.
     * @throws NullPointerException {@code date} is null.
     */
    public SubjectSource(Client client, int id, String name, Date date, URI uri) {
        this.client = checkNotNull(client, "client");
        this.id = id;
        this.name = checkNotNull(name, "name");
        this.uri = checkNotNull(uri, "uri");
        this.date = checkNotNull(date, "date");
    }

    /**
     * Returns the {@link Client} instance.
     *
     * @return {@link Client} instance.
     */
    @Override
    public Client client() {
        return client;
    }

    /**
     * Returns the subject ID/code.
     *
     * @return Subject ID/code.
     */
    public int id() {
        return id;
    }

    /**
     * Returns the subject name.
     *
     * @return Subject name.
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

