package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.client.Client

import java.net.URI
import java.util.ArrayList
import java.util.Date

/**
 * Represents a source of a content.
 *
 * @author FICTURE7
 */
interface Source {

    /**
     * Returns the [Client] instance which acquired the source.
     *
     * @return [Client] instance which acquired the source.
     */
    val client: Client

    /**
     * Returns the [Date] when the source was acquired.
     *
     * @return [Date] when the source was acquired.
     */
    val date: Date

    /**
     * Returns the [URI] of the source.
     *
     * @return [URI] of the source.
     */
    val URI: URI

    /**
     * Represents a list of [Source] instances.
     *
     * @param <T> Type of [Source].
     */
    class List<T : Source> : Iterable<T> {

        private val sources: MutableList<T>

        /**
         * Constructs a new instance of the [List] class.
         */
        constructor() {
            sources = ArrayList()
        }

        /**
         * Returns the getNumber of [Source] in the [List].
         *
         * @return Number of [Source] in the [List].
         */
        fun size(): Int {
            return sources.size
        }

        /**
         * Adds the specified [Source] instance to the [List].
         *
         * @param source [Source] instance to add.
         */
        fun add(source: T) {
            for (s in sources) {
                if (s.client.javaClass == source.client.javaClass) {
                    throw IllegalStateException("Already contains a Source instance with the same Client type.")
                }
            }

            sources.add(source)
        }

        /**
         * Removes the specified [Source] instance from the [List].
         *
         * @param source [Source] instance to remove.
         */
        fun remove(source: T) {
            sources.remove(source)
        }

        /**
         * Clears the [List].
         */
        fun clear() {
            sources.clear()
        }

        /**
         * Returns an iterator over the elements of the [List].
         * @return An iterator over the elements of the [List].
         */
        override fun iterator(): Iterator<T> {
            return sources.iterator()
        }
    }
}
