package com.ficture7.aasexplorer.model


/**
 * Represents a repository which maps keys to values and can be loaded and saved.
 *
 * @param <K> Key.
 * @param <V> Value.
 *
 * @author FICTURE7
 */
interface Repository<in K, V> : Iterable<V> {

    /**
     * Returns a boolean value indicating weather the [Repository] is loaded.
     *
     * @return A boolean value indicating weather the [Repository] is loaded.
     */
    val isLoaded: Boolean

    /**
     * Returns the value associated with the specified key.
     *
     * @param key Key.
     * @return Value associated with the specified key.
     */
    operator fun get(key: K): V?

    /**
     * Puts the specified value into the [Repository].
     *
     * @param value Value.
     */
    fun put(value: V)

    /**
     * Loads the [Repository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    fun load()

    /**
     * Loads the [Repository] asynchronously.
     *
     * @param callback Callback initializer.
     */
    fun loadAsync(callback: LoadCallback.() -> Unit)

    /**
     * Unloads the [Repository].
     */
    fun unload()

    /**
     * Saves the [Repository].
     *
     * @throws Exception When an exception is thrown.
     */
    @Throws(Exception::class)
    fun save()

    /**
     * Saves the [Repository] asynchronously.
     *
     * @param callback Callback initializer.
     */
    fun saveAsync(callback: SaveCallback.() -> Unit)

    /**
     * Callback object when a [Repository] has loaded asynchronously.
     */
    class LoadCallback {

        internal var onLoadCallback: (() -> Unit)? = null
        internal var onErrorCallback: ((Exception) -> Unit)? = null

        /**
         * Sets the onLoad callback.
         */
        fun onLoad(callback: () -> Unit) {
            onLoadCallback = callback
        }

        /**
         * Sets the onError callback.
         */
        fun onError(callback: (Exception) -> Unit) {
            onErrorCallback = callback
        }
    }

    /**
     * Callback object when a [Repository] has saved asynchronously.
     */
    class SaveCallback {

        internal var onSaveCallback: (() -> Unit)? = null
        internal var onErrorCallback: ((Exception) -> Unit)? = null

        /**
         * Sets the onSave callback.
         */
        fun onSave(callback: () -> Unit) {
            onSaveCallback = callback
        }

        /**
         * Sets the onError callback.
         */
        fun onError(callback: (Exception) -> Unit) {
            onErrorCallback = callback
        }
    }
}
