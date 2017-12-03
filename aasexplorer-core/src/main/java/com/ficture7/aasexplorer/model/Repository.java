package com.ficture7.aasexplorer.model;

/**
 * Represents a repository which maps keys to values and can be loaded and saved.
 *
 * @param <K> Key.
 * @param <V> Value.
 * @author FICTURE7
 */
public interface Repository<K, V> extends Iterable<V> {

    /**
     * Returns the value associated with the specified key.
     *
     * @param key Key.
     * @return Value associated with the specified key.
     */
    V get(K key);


    //TODO: This thing is write-only, make it read-write instead.

    /**
     * Puts the specified value into the {@link Repository}.
     *
     * @param value Value.
     */
    void put(V value);

    /**
     * Returns a boolean value indicating weather the {@link Repository} is loaded.
     *
     * @return A boolean value indicating weather the {@link Repository} is loaded.
     */
    boolean isLoaded();

    /**
     * Loads the {@link Repository}.
     *
     * @throws Exception When an exception is thrown.
     */
    void load() throws Exception;

    /**
     * Unloads the {@link Repository}.
     */
    void unload();

    /**
     * Saves the {@link Repository}.
     *
     * @throws Exception When an exception is thrown.
     */
    void save() throws Exception;
}
