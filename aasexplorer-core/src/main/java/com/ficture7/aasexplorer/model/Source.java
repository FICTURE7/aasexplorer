package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a source of a content.
 *
 * @author FICTURE7
 */
public interface Source {

    /**
     * Returns the {@link Client} instance which acquired the source.
     *
     * @return {@link Client} instance which acquired the source.
     */
    Client client();

    /**
     * Returns the {@link Date} when the source was acquired.
     *
     * @return {@link Date} when the source was acquired.
     */
    Date date();

    /**
     * Returns the {@link URI} of the source.
     *
     * @return {@link URI} of the source.
     */
    URI uri();

    /**
     * Represents a list of {@link Source} instances.
     *
     * @param <T> Type of {@link Source}.
     */
    class List<T extends Source> implements Iterable<T> {

        private final java.util.List<T> sources;

        /**
         * Constructs a new instance of the {@link List} class.
         */
        public List() {
            sources = new ArrayList<>();
        }

        /**
         * Returns the number of {@link Source} in the {@link List}.
         *
         * @return Number of {@link Source} in the {@link List}.
         */
        public int size() {
            return sources.size();
        }

        /**
         * Adds the specified {@link Source} instance to the {@link List}.
         *
         * @param source {@link Source} instance to add.
         * @throws NullPointerException {@code source} is null.
         */
        public void add(T source) {
            checkNotNull(source, "source");

            for (T s : sources) {
                if (s.client().getClass() == source.client().getClass()) {
                    throw new IllegalStateException("Already contains a Source instance with the same Client type.");
                }
            }

            sources.add(source);
        }

        /**
         * Removes the specified {@link Source} instance from the {@link List}.
         *
         * @param source {@link Source} instance to remove.
         * @throws NullPointerException {@code source} is null.
         */
        public void remove(T source) {
            checkNotNull(source, "source");

            sources.remove(source);
        }

        /**
         * Clears the {@link List}.
         */
        public void clear() {
            sources.clear();
        }

        /**
         * Returns an iterator over the elements of the {@link List}.
         * @return An iterator over the elements of the {@link List}.
         */
        @Override
        public Iterator<T> iterator() {
            return sources.iterator();
        }
    }
}
