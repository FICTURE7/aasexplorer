package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.client.Client

import java.net.URI
import java.util.Date

/**
 * Represents a [Resource] source.
 *
 * @author FICTURE7
 */
class ResourceSource : Source {

    /**
     * Constructs a new instance of the [ResourceSource] class with the specified
     * [Client] instance which acquired the [ResourceSource], resource name,
     * [Date] when the source was acquired and [URI] pointing to the source.
     *
     * @param client [Client] instance which acquired the [ResourceSource].
     * @param name   Resource name.
     * @param date   [Date] when the source was acquired.
     * @param uri    [URI] pointing to the source.
     * @throws NullPointerException `client` is null.
     * @throws NullPointerException `name` is null.
     * @throws NullPointerException `date` is null.
     * @throws NullPointerException `URI` is null.
     */
    constructor(client: Client, name: String, date: Date, uri: URI) {
        this.client = client
        this.name = name
        this.date = date
        this.URI = uri
    }

    /**
     * Returns the [Client] instance which acquired the [ResourceSource].
     *
     * @return [Client] instance which acquired the [ResourceSource].
     */
    override val client: Client
    /**
     * Returns the name of the [ResourceSource].
     *
     * @return Name of the [ResourceSource]
     */
    val name: String

    /**
     * Returns the [Date] when the source was acquired.
     *
     * @return [Date] when the source was acquired.
     */
    override val date: Date

    /**
     * Returns the [URI] pointing to the source.
     *
     * @return [URI] pointing to the source.
     */
    override val URI: URI
}
