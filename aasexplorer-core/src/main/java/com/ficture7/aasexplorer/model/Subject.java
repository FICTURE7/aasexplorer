package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a subject.
 *
 * @author FICTURE7
 */
public class Subject {

    // Subject ID/code.
    private final int id;
    // Subject name.
    private final String name;
    // Resource repository which contains the subject's resources.
    private final ResourceRepository resources;
    // List of sources.
    private final Source.List<SubjectSource> sources;

    final Class<? extends Examination> examinationClass;

    /**
     * Constructs a new instance of the {@link Subject} class with the specified {@link Examination},
     * {@link Loader}, {@link Saver}, subject ID/code and name.
     *
     * @param examination {@link Examination}.
     * @param loader      {@link Loader}.
     * @param saver       {@link Saver}.
     * @param id          Subject ID/code.
     * @param name        Subject name.
     * @throws NullPointerException {@code name} is null.
     * @throws NullPointerException {@code examination} is null.
     */
    Subject(Examination examination, Loader loader, Saver saver, int id, String name) {
        this.name = checkNotNull(name, "name");
        this.id = id;

        examinationClass = checkNotNull(examination, "examination").getClass();
        resources = new ResourceRepository(this, examination, loader, saver);
        sources = new Source.List<>();
    }

    /**
     * Returns the subject ID/code of the {@link Subject}.
     *
     * @return Subject ID/code of the {@link Subject}.
     */
    public int id() {
        return id;
    }

    /**
     * Returns the name of the {@link Subject}.
     *
     * @return Name of the {@link Subject}.
     */
    public String name() {
        return name;
    }

    /**
     * Returns the sources of the {@link Subject}.
     *
     * @return Sources of the {@link Subject}.
     */
    public Source.List<SubjectSource> sources() {
        return sources;
    }

    /**
     * Returns the resources of the {@link Subject}.
     *
     * @return Resources of the {@link Subject}.
     */
    public ResourceRepository resources() {
        return resources;
    }

    /**
     * Returns the hash code of the {@link Subject} instance.
     *
     * @return Hash code of the {@link Subject} instance.
     */
    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Returns the string representation of the {@link Subject} instance.
     *
     * @return String representation of the {@link Subject}.
     */
    @Override
    public String toString() {
        return String.format(Locale.US, "%s (%d)", name, id);
    }

    /**
     * Represents a {@link Subject} factory.
     *
     * @author FICTURE7.
     */
    public static class Factory {

        private final Examination examination;
        private final Loader loader;
        private final Saver saver;

        /**
         * Constructs a new instance of the {@link Factory} class with the specified
         * {@link Examination}, {@link Loader} and {@link Saver}.
         *
         * @param examination {@link Examination} instance.
         * @param loader      {@link Loader} instance.
         * @param saver       {@link Saver} instance.
         * @throws NullPointerException {@code examination} is null.
         * @throws NullPointerException {@code loader} is null.
         * @throws NullPointerException {@code saver} is null.
         */
        public Factory(Examination examination, Loader loader, Saver saver) {
            this.examination = checkNotNull(examination, "examination");
            this.loader = checkNotNull(loader, "loader");
            this.saver = checkNotNull(saver, "saver");
        }

        /**
         * Creates a new instance of a {@link Subject} with the specified subject ID/code
         * and name.
         *
         * @param id Subject ID/code.
         * @param name Subject name.
         * @return {@link Subject} instance.
         */
        public Subject create(int id, String name) {
            return new Subject(examination, loader, saver, id, name);
        }
    }
}
