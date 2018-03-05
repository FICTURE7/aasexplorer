package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.client.Client

import java.net.URI
import java.util.Date

/**
 * Represents a [Subject] source.
 *
 * @author FICTURE7
 */
class SubjectSource : Source {

    /**
     * Constructs a new instance of the [SubjectSource] class with the specified
     * [Client] instance which acquired the [SubjectSource], subject ID/code,
     * subject name, [Date] when the source was acquired and [URI] pointing to the source.
     *
     * @param client [Client] instance which acquired the [SubjectSource].
     * @param id     Subject ID/code.
     * @param name   Subject getName.
     * @param date   [Date] when the source was acquired.
     * @param uri    [URI] pointing to the source.
     */
    constructor(client: Client, id: Int, name: String, date: Date, uri: URI) {
        this.client = client
        this.id = id
        this.name = name
        this.date = date
        this.URI = uri
    }

    /**
     * Returns the [Client] instance.
     *
     * @return [Client] instance.
     */
    override val client: Client

    /**
     * Returns the subject ID/code.
     *
     * @return Subject ID/code.
     */
    val id: Int

    /**
     * Returns the subject name.
     *
     * @return Subject name.
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

