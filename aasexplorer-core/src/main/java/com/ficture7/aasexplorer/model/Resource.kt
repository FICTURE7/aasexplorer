package com.ficture7.aasexplorer.model

/**
 * Represents a resource of a [Subject].
 *
 * @author FICTURE7
 */
open class Resource {

    /**
     * Constructs a new instance of the [Resource] class with the specified resource name.
     *
     * @param name Resource name.
     */
    constructor(name: String) {
        this.name = name
    }

    /**
     * Returns the name of the [Resource].
     *
     * @return Name of the [Resource].
     */
    val name: String

    /**
     * Returns the sources of the [Resource].
     *
     * @return Sources of the [Resource].
     */
    val sources: Source.List<ResourceSource> = Source.List()
}

