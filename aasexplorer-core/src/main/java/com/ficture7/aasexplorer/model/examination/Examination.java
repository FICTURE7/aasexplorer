package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.SubjectRepository;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents an examination.
 *
 * @author FICTURE7
 */
public abstract class Examination {

    // Subject repository which contains the subject instances.
    private final SubjectRepository subjects;

    /**
     * Constructs a new instance of the {@link Examination} class with the specified {@link Loader}
     * and {@link Saver} which will be passed to {@link Examination#subjects()}.
     *
     * @param loader {@link Loader} instance.
     * @param saver  {@link Saver} instance.
     * @throws NullPointerException {@code loader} is null.
     * @throws NullPointerException {@code saver} is null.
     */
    protected Examination(Loader loader, Saver saver) {
        checkNotNull(loader, "loader");
        checkNotNull(saver, "saver");

        subjects = new SubjectRepository(this, loader, saver);
    }

    /**
     * Returns the name of the {@link Examination}.
     *
     * @return Name of the {@link Examination}.
     */
    public abstract String name();

    /**
     * Returns the {@link SubjectRepository} of the {@link Examination}.
     *
     * @return {@link SubjectRepository} of the {@link Examination}.
     */
    public SubjectRepository subjects() {
        return subjects;
    }
}
