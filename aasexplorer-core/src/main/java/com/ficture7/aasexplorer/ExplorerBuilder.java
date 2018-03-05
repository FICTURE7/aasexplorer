package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.store.Store;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotAbstract;
import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotInterface;
import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Provides a fluent interface to build {@link Explorer} instances.
 *
 * @author FICTURE7
 */
public class ExplorerBuilder {

    ClassWithInitializer<? extends CallbackExecutor> executorClass;
    ClassWithInitializer<? extends ExplorerLoader> loaderClass;
    ClassWithInitializer<? extends ExplorerSaver> saverClass;
    ClassWithInitializer<? extends Store> storeClass;

    final List<ClassWithInitializer<? extends Client>> clientClasses;
    final List<ClassWithInitializer<? extends Examination>> examinationClasses; //TODO: Examinations does not require initializers.

    /**
     * Constructs a new instance of the {@link ExplorerBuilder} class.
     */
    public ExplorerBuilder() {
        clientClasses = new ArrayList<>();
        examinationClasses = new ArrayList<>();
    }

    /**
     * Adds the a new instance of the specified {@link Examination} class.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T>              Type of {@link Examination}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code examinationClass} is either null or an abstract class.
     */
    public <T extends Examination> ExplorerBuilder withExamination(Class<T> examinationClass) {
        checkNotNull(examinationClass, "examinationClass");
        checkNotAbstract(examinationClass, "examinationClass");

        examinationClasses.add(new ClassWithInitializer<>(examinationClass, null));
        return this;
    }

    /**
     * Adds the a new instance of the specified {@link Client} class.
     *
     * @param clientClass {@link Client} class.
     * @param <T>         Type of {@link Client}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code clientClass} is either null, an abstract class or an interface.
     */
    public <T extends Client> ExplorerBuilder withClient(Class<T> clientClass) {
        return withClient(clientClass, null);
    }

    /**
     * Adds the a new instance of the specified {@link Client} class with the specified {@link Initializer}.
     *
     * @param clientClass       {@link Client} class.
     * @param clientInitializer {@link Initializer} to initialize the {@link Client} instance.
     * @param <T>               Type of {@link Client}
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code clientClass} is either null, an abstract class or an interface.
     */
    public <T extends Client> ExplorerBuilder withClient(Class<T> clientClass, Initializer<T> clientInitializer) {
        checkNotNull(clientClass, "clientClass");
        checkNotAbstract(clientClass, "clientClass");
        checkNotInterface(clientClass, "clientClass");

        clientClasses.add(new ClassWithInitializer<>(clientClass, clientInitializer));
        return this;
    }

    @NotNull
    public <T extends CallbackExecutor> ExplorerBuilder useExecutor(@NotNull Class<T> executorClass) {
        return useExecutor(executorClass, null);
    }

    @NotNull
    public <T extends CallbackExecutor> ExplorerBuilder useExecutor(@NotNull Class<T> executorClass, Initializer<T> executorInitializer) {
        this.executorClass = new ClassWithInitializer<>(executorClass, executorInitializer);
        return this;
    }

    /**
     * Uses the specified {@link Store} type.
     *
     * @param storeClass {@link Store} class.
     * @param <T>        Type of {@link Store}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code storeClass} is either null or an abstract class.
     */
    public <T extends Store> ExplorerBuilder useStore(Class<T> storeClass) {
        return useStore(storeClass, null);
    }

    /**
     * Uses the specified {@link Store} type with the specified {@link Initializer}.
     *
     * @param storeClass       {@link Store} class.
     * @param storeInitializer {@link Initializer} to initialize the {@link Store} instance.
     * @param <T>              Type of {@link Store}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code storeClass} is either null or an abstract class.
     */
    public <T extends Store> ExplorerBuilder useStore(Class<T> storeClass, Initializer<T> storeInitializer) {
        checkNotNull(storeClass, "storeClass");
        checkNotAbstract(storeClass, "storeClass");

        this.storeClass = new ClassWithInitializer<>(storeClass, storeInitializer);
        return this;
    }

    /**
     * Uses the specified {@link Loader} type.
     *
     * @param loaderClass {@link Loader} class.
     * @param <T>         Type of {@link Loader}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code loaderClass} is either null or an abstract class.
     */
    public <T extends ExplorerLoader> ExplorerBuilder useLoader(Class<T> loaderClass) {
        return useLoader(loaderClass, null);
    }

    /**
     * Uses the specified {@link Loader} type with the specified {@link Initializer}.
     *
     * @param loaderClass       {@link Loader} class.
     * @param loaderInitializer {@link Initializer} to initialize the {@link Loader} instance.
     * @param <T>               Type of {@link Loader}
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code loaderClass} is either null or an abstract class.
     */
    public <T extends ExplorerLoader> ExplorerBuilder useLoader(Class<T> loaderClass, Initializer<T> loaderInitializer) {
        checkNotNull(loaderClass, "loaderClass");
        checkNotAbstract(loaderClass, "loaderClass");

        this.loaderClass = new ClassWithInitializer<>(loaderClass, loaderInitializer);
        return this;
    }

    /**
     * Uses the specified {@link Saver} type.
     *
     * @param saverClass {@link Saver} class.
     * @param <T>        Type of {@link Saver}.
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code saverClass} is either null or an abstract class.
     */
    public <T extends ExplorerSaver> ExplorerBuilder useSaver(Class<T> saverClass) {
        return useSaver(saverClass, null);
    }

    /**
     * Uses the specified {@link Saver} type with the specified {@link Initializer}.
     *
     * @param saverClass       {@link Saver} class.
     * @param saverInitializer {@link Initializer} to initialize the {@link Saver} instance.
     * @param <T>              Type of {@link Saver}
     * @return {@link ExplorerBuilder} instance.
     * @throws IllegalArgumentException {@code saverClass} is either null or an abstract class.
     */
    public <T extends ExplorerSaver> ExplorerBuilder useSaver(Class<T> saverClass, Initializer<T> saverInitializer) {
        this.saverClass = new ClassWithInitializer<>(saverClass, saverInitializer);
        return this;
    }

    /**
     * Returns the {@link Explorer} instance.
     *
     * @return {@link Explorer} instance.
     */
    public Explorer build() throws ExplorerBuilderException {
        if (storeClass == null) {
            throw new ExplorerBuilderException("Store was not set.");
        }

        // Fallback loader and saver to default one if not set.
        if (loaderClass == null) {
            loaderClass = new ClassWithInitializer<>(ExplorerLoader.class, null);
        }
        if (saverClass == null) {
            saverClass = new ClassWithInitializer<>(ExplorerSaver.class, null);
        }

        // Fallback getExecutor to default one if not set.
        if (executorClass == null) {
            executorClass = new ClassWithInitializer<>(ExplorerExecutor.class, null);
        }

        try {
            return new Explorer(this);
        } catch (ExplorerBuilderException e) {
            throw e;
        } catch (Exception e) {
            throw new ExplorerBuilderException("Failed to build Explorer instance: " + e.getMessage(), e);
        }
    }

    /**
     * Represents a initializer which initializes object instances.
     *
     * @param <T> Type of object.
     * @author FICTURE7
     */
    public interface Initializer<T> {

        /**
         * Called to initialize the instance.
         *
         * @param instance Store instance.
         */
        void init(T instance);
    }

    /**
     * Attaches a {@link Class} to an {@link Initializer} of the same type.
     *
     * @param <T> Type.
     */
    static final class ClassWithInitializer<T> {

        /**
         * Constructs a new instance of the {@link ClassWithInitializer} class with the specified
         * {@link Class} and {@link Initializer}.
         *
         * @param tClass       Class.
         * @param tInitializer Initializer.
         * @throws IllegalArgumentException {@code tClass} is null.
         */
        ClassWithInitializer(Class<T> tClass, Initializer<T> tInitializer) {
            this.tClass = checkNotNull(tClass, "tClass");
            this.tInitializer = tInitializer;
        }

        /**
         * Class instance.
         */
        final Class<T> tClass;

        /**
         * Initializer instance.
         */
        final Initializer<T> tInitializer;

        /**
         * Creates a new instance of an object of type {@code tClass} using the specified constructor and parameters and initializes it
         * using {@code tInitializer}.
         *
         * @param constructorClasses Constructor classes.
         * @param constructorParams  Constructor parameters.
         * @return New instance of an object of type {@code tClass}.
         */
        T newInstance(Class<?>[] constructorClasses, Object[] constructorParams) throws ExplorerBuilderException {
            Constructor<T> ctor;

            try {
                ctor = tClass.getConstructor(constructorClasses);
            } catch (NoSuchMethodException e) {
                throw new ExplorerBuilderException("No constructor for " + e.getMessage(), e);
            }

            T instance;
            try {
                instance = ctor.newInstance(constructorParams);
            } catch (Exception e) {
                throw new ExplorerBuilderException(e);
            }

            if (tInitializer != null) {
                tInitializer.init(instance);
            }

            return instance;
        }
    }
}

