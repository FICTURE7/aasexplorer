package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a {@link Repository repository} of {@link Subject subjects}.
 *
 * @author FICTURE7
 */
public class SubjectRepository implements Repository<Integer, Subject> {

    //TODO: Create some form factory to create Subject & Resource instances.

    private boolean isLoaded;

    private final Examination examination;
    private final Class<? extends Examination> examinationClass;

    private final Loader loader;
    private final Saver saver;

    // Maps subject ID/codes to subject instances.
    private final Map<Integer, Subject> subjects;

    /**
     * Constructs a new instance of the {@link SubjectRepository} class with the
     * specified {@link Examination}, {@link Loader} and {@link Saver}.
     *
     * @param examination {@link Examination} instance.
     * @param loader      {@link Loader} instance.
     * @param saver       {@link Saver} instance.
     * @throws IllegalArgumentException {@code examination} is null.
     * @throws IllegalArgumentException {@code loader} is null.
     * @throws IllegalArgumentException {@code saver} is null.
     */
    public SubjectRepository(Examination examination, Loader loader, Saver saver) {
        this.examination = checkNotNull(examination, "examination");
        this.loader = checkNotNull(loader, "loader");
        this.saver = checkNotNull(saver, "saver");

        subjects = new HashMap<>();
        examinationClass = examination.getClass();
    }

    /**
     * Returns the {@link Subject} with the specified subject ID/code.
     *
     * @param subjectId Subject ID/code.
     * @return {@link Subject} with the specified subject ID/code.
     */
    @Override
    public Subject get(Integer subjectId) {
        return subjects.get(subjectId);
    }

    /**
     * Puts the specified {@link Subject} instance to the {@link SubjectRepository}.
     *
     * @param subject {@link Subject} instance.
     * @throws IllegalArgumentException {@code subject} is null.
     */
    public void put(Subject subject) {
        checkNotNull(subject, "subject");

        subjects.put(subject.id(), subject);
    }

    /**
     * Returns a boolean value indicating weather the {@link SubjectRepository} is loaded.
     *
     * @return A boolean value indicating weather the {@link SubjectRepository} is loaded.
     */
    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Loads the {@link SubjectRepository}.
     *
     * @throws Exception When an exception is thrown.
     */
    @Override
    public void load() throws Exception {
        // Unloads the repository to prevent duplication.
        unload();

        // Get the subject sources from the loader which can come from a
        // a client or a store.
        Iterable<SubjectSource> sources = loader.loadSubjects(examinationClass);

        for (SubjectSource source : sources) {
            Subject subject = get(source.id());

            if (subject != null) {
                subject.sources().add(source);
            } else {
                subject = new Subject(examination, loader, saver, source.id(), source.name());
                subject.sources().add(source);

                put(subject);
            }
        }

        isLoaded = true;
    }

    /**
     * Unloads the {@link SubjectRepository}.
     */
    @Override
    public void unload() {
        subjects.clear();
        isLoaded = false;
    }

    /**
     * Saves the {@link SubjectRepository}.
     *
     * @throws Exception When an exception is thrown.
     */
    @Override
    public void save() throws Exception {
        // Merge all the subject sources into a single linear list.
        List<SubjectSource> sources = new ArrayList<>();
        for (Subject subject : this) {
            for (SubjectSource source : subject.sources()) {
                sources.add(source);
            }
        }

        // Pass the subjects to save to the saver instance.
        saver.saveSubjects(examinationClass, sources);
    }

    /**
     * Returns an iterator through the {@link Subject} in the {@link SubjectRepository}.
     *
     * @return An iterator through the {@link Subject} in the {@link SubjectRepository}.
     */
    @Override
    public Iterator<Subject> iterator() {
        return subjects.values().iterator();
    }
}
