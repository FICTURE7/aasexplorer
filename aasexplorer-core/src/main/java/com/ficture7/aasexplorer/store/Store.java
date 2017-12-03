package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.Source;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a store which stores {@link Source}.
 *
 * @author FICTURE7
 */
public abstract class Store implements Loader, Saver {

    private final Explorer explorer;

    /**
     * Constructs a new instance of the {@link Store} class with the specified
     * {@link Explorer} instance.
     *
     * @param explorer {@link Explorer} instance.
     * @throws NullPointerException {@code explorer} is null.
     */
    public Store(Explorer explorer) {
        this.explorer = checkNotNull(explorer, "explorer");
    }

    /**
     * Returns the {@link Client} instance of the specified class type which is in the {@link Explorer}.
     *
     * @param clientClassName {@link Client} class name.
     * @return {@link Client} instance; null if not found or class does not exists.
     * @throws NullPointerException {@code clientClassName} is null.
     */
    protected final Client getClient(String clientClassName) {
        checkNotNull(clientClassName, "clientClassName");

        // Try to get a Class matching the specified client class string.
        Class clientClass;
        try {
            clientClass = Class.forName(clientClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }

        // Return the client instance from the getExplorer which maybe null if not found.
        return explorer.clients().get(clientClass);
    }
}
