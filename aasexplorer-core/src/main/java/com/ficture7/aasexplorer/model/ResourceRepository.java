package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Represents a repository of {@link Resource}.
 *
 * @author FICTURE7
 */
public class ResourceRepository implements Repository<String, Resource>, Iterable<Resource> {

    //TODO: Create a custom collection for resources to filter them by date and stuff.

    private boolean isLoaded;

    // Subject instance which owns this ResourceRepository.
    private final Subject subject;
    private final Loader loader;
    private final Saver saver;

    // Maps resource name to resource instances.
    private final Map<String, Resource> resources;

    // Pre-sorted lists of resources.
    private final List<MarkingScheme> markingSchemes;
    private final List<QuestionPaper> questionPapers;
    private final List<Resource> others;

    // Read-only collections to prevent leaks.
    private final Collection<MarkingScheme> readOnlyMarkingSchemes;
    private final Collection<QuestionPaper> readOnlyQuestionPapers;
    private final Collection<Resource> readOnlyOthers;

    private final Class<? extends Examination> examinationClass;

    /**
     * Constructs a new instance of th e{@link ResourceRepository} with the specified {@link Subject}
     * instance which owns this {@link ResourceRepository}, {@link Loader} and {@link Saver}.
     *
     * @param subject {@link Subject} instance which owns this {@link ResourceRepository}.
     * @param loader  {@link Loader} instance.
     * @param saver   {@link Saver} instance.
     * @throws NullPointerException {@code subject} is null.
     */
    ResourceRepository(Subject subject, Examination examination, Loader loader, Saver saver) {
        this.subject = checkNotNull(subject, "subject");
        this.loader = checkNotNull(loader, "loader");
        this.saver = checkNotNull(saver, "saver");

        examinationClass = checkNotNull(examination, "examination").getClass();

        resources = new HashMap<>();

        markingSchemes = new ArrayList<>();
        questionPapers = new ArrayList<>();
        others = new ArrayList<>();

        readOnlyMarkingSchemes = Collections.unmodifiableCollection(markingSchemes);
        readOnlyQuestionPapers = Collections.unmodifiableCollection(questionPapers);
        readOnlyOthers = Collections.unmodifiableCollection(others);
    }

    /**
     * Returns an iterable which contains only {@link MarkingScheme}.
     *
     * @return An iterable which contains only {@link MarkingScheme}.
     */
    public Iterable<MarkingScheme> markingSchemes() {
        return readOnlyMarkingSchemes;
    }

    /**
     * Returns an iterable which contains only {@link QuestionPaper}.
     *
     * @return An iterable which contains only {@link QuestionPaper}.
     */
    public Iterable<QuestionPaper> questionPapers() {
        return readOnlyQuestionPapers;
    }

    /**
     * Returns an iterable which contains other unidentified {@link Resource}s.
     *
     * @return An iterable which contains other unidentified {@link Resource}s.
     */
    public Iterable<Resource> others() {
        return readOnlyOthers;
    }

    /**
     * Returns the number of {@link Resource} which is in the repository.
     *
     * @return Number of {@link Resource} which is in the repository.
     */
    public int size() {
        return resources.size();
    }

    /**
     * Returns the {@link Resource} with the specified name.
     * Returns null if no {@link Resource} with the specified name is in the repository.
     *
     * @param resourceName Resource name.
     * @return {@link Resource} with the specified name.
     */
    public Resource get(String resourceName) {
        return resources.get(resourceName);
    }

    /**
     * Puts the specified {@link Resource} to the repository.
     *
     * @param resource {@link Resource} to put in the repository.
     */
    public void put(Resource resource) {
        checkNotNull(resource, "resource");

        // Add the Resource instance to the Map
        // mapping resource names to resource instances.
        resources.put(resource.name(), resource);

        // Add the Resource instance to the corresponding list.
        if (resource instanceof MarkingScheme) {
            markingSchemes.add((MarkingScheme) resource);
        } else if (resource instanceof QuestionPaper) {
            questionPapers.add((QuestionPaper) resource);
        } else {
            others.add(resource);
        }
    }

    /**
     * Returns a boolean value indicating weather the {@link ResourceRepository} is loaded.
     *
     * @return A boolean value indicating weather the {@link ResourceRepository} is loaded.
     */
    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Loads the {@link Repository}.
     *
     * @throws Exception When an exception is thrown.
     */
    @Override
    public void load() throws Exception {
        unload();

        Iterable<ResourceSource> sources = loader.loadResources(subject);

        for (ResourceSource source : sources) {
            Resource resource = get(source.name());

            if (resource == null) {
                // Turn the resource source to the resource type.
                // E.G: MarkingScheme, QuestionPaper or Resource it self if
                // unable to identified.
                resource = resourceSourceToResource(source);
                resource.sources().add(source);

                put(resource);
            } else {
                resource.sources().add(source);
            }
        }

        isLoaded = true;
    }

    /**
     * Unloads the {@link Repository}.
     */
    @Override
    public void unload() {
        resources.clear();
        isLoaded = false;
    }

    /**
     * Saves the {@link Repository}.
     *
     * @throws Exception When an exception is thrown.
     */
    @Override
    public void save() throws Exception {
        List<ResourceSource> resourceSources = new ArrayList<>();
        for (Resource resource : this) {
            for (ResourceSource source : resource.sources()) {
                resourceSources.add(source);
            }
        }

        saver.saveResources(examinationClass, subject, resourceSources);
    }

    /**
     * Returns an iterator over the {@link Resource} of the repository.
     *
     * @return An iterator over the {@link Resource} of the repository.
     */
    @Override
    public Iterator<Resource> iterator() {
        return resources.values().iterator();
    }

    private Resource resourceSourceToResource(ResourceSource source) {
        //TODO: Improve the logic n stuff. Defensive stuff.
        String name = source.name();
        try {
            int index1;
            int index2;
            int index3;

            index1 = name.indexOf('_');
            index2 = name.indexOf('_', index1 + 1);
            index3 = name.indexOf('_', index2 + 1);

            if (index1 == -1 || index2 == -1 || index3 == -1) {
                return new Resource(name);
            }

            String id = name.substring(0, index1);
            String session = name.substring(index1 + 1, index2);
            String type = name.substring(index2 + 1, index3);

            switch (type) {
                case "qp":
                    return new QuestionPaper(name, Session.parse(session));
                case "ms":
                    return new MarkingScheme(name, Session.parse(session));
                default:
                    return new Resource(name);
            }
        }
        catch (Exception e) {
            // Fallback to a Resource instance if we fail to parse the name.
            return new Resource(name);
        }
    }
}

