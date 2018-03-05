package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.CallbackExecutor;
import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.Source;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a getStore which stores {@link Source}.
 *
 * @author FICTURE7
 */
public abstract class Store implements Loader, Saver {

    private final CallbackExecutor executor;
    private final Explorer explorer;

    /**
     * Constructs a new instance of the {@link Store} class with the specified
     * {@link Explorer} instance.
     *
     * @param explorer {@link Explorer} instance.
     * @throws NullPointerException {@code explorer} is null.
     */
    public Store(@NotNull Explorer explorer) {
        this.explorer = explorer;
        this.executor = explorer.getExecutor();
    }

    @Override
    public CallbackExecutor getExecutor() {
        return executor;
    }

    /**
     * Returns the {@link Client} instance of the specified class type which is in the {@link Explorer}.
     *
     * @param clientClassName {@link Client} class getName.
     * @return {@link Client} instance; null if not found or class does not exists.
     */
    protected final Client getClient(@NotNull String clientClassName) {
        // Try to get a Class matching the specified getClient class string.
        Class clientClass;
        try {
            clientClass = Class.forName(clientClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }

        // Return the getClient instance from the explorer which maybe null if not found.
        return explorer.getClients().get(clientClass);
    }
}
