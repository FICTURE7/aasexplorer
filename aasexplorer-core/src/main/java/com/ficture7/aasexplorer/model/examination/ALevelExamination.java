package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

/**
 * Represents the A & AS level examination.
 *
 * @author FICTURE7
 */
public class ALevelExamination extends Examination {

    /**
     * Constructs a new instance of the {@link ALevelExamination} class with the specified {@link Loader}
     * and {@link Saver} which will be passed to {@link Examination#subjects()}.
     *
     * @param loader {@link Loader} instance.
     * @param saver  {@link Saver} instance.
     * @throws NullPointerException {@code loader} is null.
     * @throws NullPointerException {@code saver} is null.
     */
    public ALevelExamination(Loader loader, Saver saver) {
        super(loader, saver);
    }

    /**
     * Returns the name of the {@link Examination}.
     *
     * @return Name of the {@link Examination}.
     */
    @Override
    public String name() {
        return "A & AS Level";
    }
}
