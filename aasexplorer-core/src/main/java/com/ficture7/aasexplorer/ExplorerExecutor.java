package com.ficture7.aasexplorer;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Default {@link CallbackExecutor} used by {@link Explorer}.
 *
 * @author FICTURE7
 */
public class ExplorerExecutor implements CallbackExecutor {

    private final ExecutorService service;

    /**
     * Constructs a new instance of the {@link ExplorerExecutor} class.
     */
    public ExplorerExecutor() {
        service = Executors.newFixedThreadPool(2);
    }

    /**
     * Returns the {@link Executor} which will be used to execute callbacks.
     *
     * @return {@link Executor} which will be used to execute callbacks.
     */
    @NotNull
    @Override
    public Executor getCallbackExecutor() {
        return this;
    }

    /**
     * Executes the specified {@link Runnable}.
     *
     * @param runnable {@link Runnable} to execute.
     */
    @Override
    public void execute(@NotNull Runnable runnable) {
        service.execute(runnable);
    }
}
