package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.model.examination.OLevelExamination;
import com.ficture7.aasexplorer.store.Store;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents the core explorer and provides context for stuff to work.
 *
 * @author FICTURE7
 */
public class Explorer {

    // Loader that will load stuff.
    final ExplorerLoader loader;
    // Saver that will save stuff.
    final ExplorerSaver saver;

    // Store which will save and load data.
    final Store store;

    private final CallbackExecutor executor;
    //TODO: Add some form of priority list.
    // Collection of getClients to use.
    private final Clients clients;
    // Collection of getExaminations.
    private final Examinations examinations;

    /**
     * Constructs a new instance of the {@link Explorer} class with the specified
     * {@link ExplorerBuilder} instance.
     *
     * @param builder {@link ExplorerBuilder} instance.
     * @throws NullPointerException {@code builder} is null.
     * @throws Exception            When an issue happens.
     */
    Explorer(ExplorerBuilder builder) throws Exception {
        checkNotNull(builder, "builder");

        clients = new Clients();
        examinations = new Examinations();

        // Avoid creating duplicated instances of the same arrays.
        // Constructor signature.
        Class<?>[] ctorExplorerClass = new Class<?>[] {Explorer.class};
        // Constructor parameters.
        Object[] ctorExplorerParam = new Object[] {this};

        loader = builder.loaderClass.newInstance(ctorExplorerClass, ctorExplorerParam);
        saver = builder.saverClass.newInstance(ctorExplorerClass, ctorExplorerParam);
        store = builder.storeClass.newInstance(ctorExplorerClass, ctorExplorerParam);

        executor = builder.executorClass.newInstance(new Class[] {}, new Object[] {});

        for (ExplorerBuilder.ClassWithInitializer<? extends Client> clientClass : builder.clientClasses) {
            Client client = clientClass.newInstance(null, null);
            clients.add(client);
        }

        // Avoid creating duplicated instances of the same arrays.
        // Constructor signature.
        Class<?>[] ctorLoaderSaverClass = new Class<?>[] {Loader.class, Saver.class};
        // Constructor parameters.
        Object[] ctorLoaderSaverParam = new Object[] {loader, saver};

        for (ExplorerBuilder.ClassWithInitializer<? extends Examination> examinationClass : builder.examinationClasses) {
            Examination examination = examinationClass.newInstance(ctorLoaderSaverClass, ctorLoaderSaverParam);
            examinations.add(examination);
        }
    }

    /**
     * Returns the {@link CallbackExecutor} which will be used for async operations.
     *
     * @return {@link CallbackExecutor} which will be used for async operations.
     */
    public CallbackExecutor getExecutor() {
        return executor;
    }

    /**
     * Returns the {@link ExplorerLoader} of the {@link Explorer}.
     *
     * @return {@link ExplorerLoader} of the {@link Explorer}.
     */
    @NotNull
    public ExplorerLoader getLoader() {
        return loader;
    }

    /**
     * Returns the {@link ExplorerSaver} of the {@link Explorer}.
     *
     * @return {@link ExplorerSaver} of the {@link Explorer}.
     */
    @NotNull
    public ExplorerSaver getSaver() {
        return saver;
    }

    /**
     * Returns the {@link Store} of the {@link Explorer}.
     *
     * @return {@link Store} instance of the {@link Explorer}.
     */
    @NotNull
    public Store getStore() {
        return store;
    }

    /**
     * Returns the list of {@link Client}s.
     *
     * @return List of {@link Client}s.
     */
    @NotNull
    public Clients getClients() {
        return clients;
    }

    /**
     * Returns the list of {@link Examination}s.
     *
     * @return List of {@link Examination}s.
     */
    @NotNull
    public Examinations getExaminations() {
        return examinations;
    }

    /**
     * Returns the {@link ALevelExamination} of the explorer; returns null if not found.
     *
     * @return {@link ALevelExamination} of the explorer; returns null if not found.
     */
    public ALevelExamination getALevel() {
        return examinations.get(ALevelExamination.class);
    }

    /**
     * Returns the {@link OLevelExamination} of the explorer; returns null if not found.
     *
     * @return {@link OLevelExamination} of the explorer; returns null if not found.
     */
    public OLevelExamination getOLevel() {
        return examinations.get(OLevelExamination.class);
    }

    /**
     * Represents a collection of {@link Examination}.
     *
     * @author FICTURE7
     */
    public final class Examinations implements Iterable<Examination> {

        // Map Class<T extends Examination> instances to Examination instances.
        private final Map<Class<? extends Examination>, Examination> examinationMap;

        /**
         * Constructs a new instance of the {@link Examinations} class.
         */
        private Examinations() {
            examinationMap = new HashMap<>();
        }

        /**
         * Returns the getNumber of {@link Examination} which are in the collection.
         *
         * @return Number of {@link Examination} which are in the collection.
         */
        public int size() {
            return examinationMap.size();
        }

        /**
         * Adds the speicified {@link Examination} instance to the collection.
         *
         * @param examination {@link Examination} instance.
         * @param <T>         Type of {@link Examination}.
         */
        <T extends Examination> void add(T examination) {
            checkNotNull(examination, "examination");

            Class<? extends Examination> examinationClass = examination.getClass();
            if (examinationMap.containsKey(examinationClass)) {
                throw new IllegalStateException("Examinations instance already contains an Examination of same type.");
            }

            examinationMap.put(examination.getClass(), examination);
        }

        /**
         * Returns the {@link Examination} instance of the specified type; returns null if not found.
         *
         * @param examinationClass {@link Examination} class.
         * @param <T>              Type of {@link Examination}.
         * @return {@link Examination} instance of the specified type; returns null if not found.
         * @throws IllegalArgumentException {@code examinationClass} is null.
         */
        public <T extends Examination> T get(Class<T> examinationClass) {
            checkNotNull(examinationClass, "examinationClass");

            Examination examination = examinationMap.get(examinationClass);
            if (examination == null) {
                return null;
            }

            return examinationClass.cast(examination);
        }

        /**
         * Returns an iterator over the elements of the collection.
         *
         * @return An iterator over the elements of the collection.
         */
        @Override
        public Iterator<Examination> iterator() {
            return examinationMap.values().iterator();
        }
    }

    /**
     * Represents a collection of {@link Client}.
     *
     * @author FICTURE7
     */
    public final class Clients implements Iterable<Client> {

        // Map Class<T extends Client> instances to Client instances.
        private final Map<Class<? extends Client>, Client> clientMap;

        /**
         * Constructs a new instance of the {@link Clients} class.
         */
        public Clients() {
            clientMap = new HashMap<>();
        }

        /**
         * Returns the getNumber of {@link Client} which are in the collection.
         *
         * @return Number of {@link Client} which are in the collection.
         */
        public int size() {
            return clientMap.size();
        }

        /**
         * Adds the specified {@link Client} instance to the collection.
         *
         * @param client {@link Client} instance.
         * @param <T>    Type.
         * @throws NullPointerException  {@code getClient} is null.
         * @throws IllegalStateException Already contains a {@link Client} instance of the type of {@code getClient}.
         */
        <T extends Client> void add(T client) {
            checkNotNull(client, "getClient");

            Class<? extends Client> clientClass = client.getClass();
            if (clientMap.containsKey(clientClass)) {
                throw new IllegalStateException("Clients instance already contains a Client of same type.");
            }

            clientMap.put(client.getClass(), client);
        }

        /**
         * Returns the {@link Client} instance of the specified type; returns null if not found.
         *
         * @param clientClass {@link Client} class.
         * @param <T>         Type of {@link Client}.
         * @return {@link Client} instance of the specified type; returns null if not found.
         * @throws NullPointerException {@code clientClass} is null.
         */
        public <T extends Client> T get(Class<T> clientClass) {
            checkNotNull(clientClass, "clientClass");

            Client client = clientMap.get(clientClass);
            if (client == null) {
                return null;
            }

            return clientClass.cast(client);
        }

        /**
         * Returns an iterator over the elements of the collection.
         *
         * @return An iterator over the elements of the collection.
         */
        @Override
        public Iterator<Client> iterator() {
            return clientMap.values().iterator();
        }
    }
}

