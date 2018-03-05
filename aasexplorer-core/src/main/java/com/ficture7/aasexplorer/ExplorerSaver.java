package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.concurrent.Executor;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Default {@link Saver} used by {@link Explorer}.
 *
 * @author FICTURE7
 */
public class ExplorerSaver implements Saver {

    // Explorer instance which owns this ExplorerLoader instance.
    private final Explorer explorer;

    /**
     * Constructs a new instance of the {@link ExplorerSaver} class with the specified {@link Explorer} instance.
     *
     * @param explorer {@link Explorer} instance.
     * @throws IllegalArgumentException {@code explorer} is null.
     */
    public ExplorerSaver(Explorer explorer) {
        this.explorer = checkNotNull(explorer, "explorer");
    }

    /**
     * Returns the {@link Executor} which will be used for async operations.
     *
     * @return {@link Executor} which will be used for async operations.
     */
    @Override
    public CallbackExecutor getExecutor() {
        return explorer.getExecutor();
    }

    /**
     * Returns the {@link Explorer} instance which owns the {@link ExplorerLoader}.
     *
     * @return {@link Explorer} instance which owns the {@link ExplorerLoader}.
     */
    protected Explorer explorer() {
        return explorer;
    }

    @Override
    public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {
        explorer.getStore().saveSubjects(examinationClass, subjectSources);
    }

    @Override
    public void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {
        explorer.getStore().saveResources(subject, resourceSources);
    }
}
