package com.ficture7.aasexplorer.model.examination;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;

public class OLevelExamination extends Examination {

    /**
     * Constructs a new instance of the {@link OLevelExamination} class with the specified {@link Loader}
     * and {@link Saver} which will be passed to {@link Examination#subjects()}.
     *
     * @param loader {@link Loader} instance.
     * @param saver  {@link Saver} instance.
     * @throws NullPointerException {@code loader} is null.
     * @throws NullPointerException {@code saver} is null.
     */
    public OLevelExamination(Loader loader, Saver saver) {
        super(loader, saver);
    }

    /**
     * Returns the name of the {@link Examination}.
     *
     * @return Name of the {@link Examination}.
     */
    @Override
    public String name() {
        return "O Level";
    }
}
