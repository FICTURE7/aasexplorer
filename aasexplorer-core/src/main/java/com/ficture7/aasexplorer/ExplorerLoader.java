package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.store.Store;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

/**
 * Default {@link Loader} used by {@link Explorer}.
 *
 * @author FICTURE7
 */
public class ExplorerLoader implements Loader {

    // Number of days before data loaded from a Store instance is considered
    // expired and therefore data must be retrieved from Client(s) instances instead.
    private int daysBeforeExpiration;
    // Explorer instance which owns this ExplorerLoader instance.
    private final Explorer explorer;

    /**
     * Constructs a new instance of the {@link ExplorerLoader} class with the specified
     * {@link Explorer} instance.
     *
     * @param explorer {@link Explorer} instance.
     * @throws IllegalArgumentException {@code explorer} is null.
     */
    public ExplorerLoader(Explorer explorer) {
        this.explorer = checkNotNull(explorer, "explorer");

        setDaysBeforeExpiration(7);
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

    /**
     * Returns the getNumber of days before stored data is considered expired.
     *
     * @return Number of days before stored data is considered expired.
     */
    public int getDaysBeforeExpiration() {
        return daysBeforeExpiration;
    }

    /**
     * Sets the getNumber of days before stored data is considered expired.
     *
     * @param days Number of days before data is considered expired.
     */
    public void setDaysBeforeExpiration(int days) {
        daysBeforeExpiration = days;
    }

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T> Type of {@link Examination}.
     * @return {@link SubjectSource}s; can return null under certain circumstances.
     * @throws NullPointerException {@code examinationClass} is null.
     * @throws Exception Exception when loading the {@link SubjectSource}s.
     */
    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        checkNotNull(examinationClass, "examinationClass");

        // Try to load from disk first.
        Iterable<SubjectSource> sources = loadSubjectsFromStore(examinationClass);
        if (sources == null) {
            // Load from the internet if we can't for some reason.
            return loadSubjectsFromClients(examinationClass);
        }

        Iterator<SubjectSource> iterator = sources.iterator();
        if (!iterator.hasNext()) {
            // Load from the internet if the getSources are empty for some reason.
            sources = loadSubjectsFromClients(examinationClass);
        } else if (hasExpired(iterator.next().getDate())) {
            // Load from the internet if the stored data is considered expired.
            sources = loadSubjectsFromClients(examinationClass);
        }
        return sources;
    }

    /**
     * Loads the {@link ResourceSource}s for the specified {@link Subject} instance.
     *
     * @param subject {@link Subject} instance.
     * @return {@link ResourceSource}s; can return null under certain circumstances.
     * @throws NullPointerException {@code subject} is null.
     * @throws Exception Exception  when loading the {@link ResourceSource}s.
     */
    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        checkNotNull(subject, "subject");

        // Try to load from disk first.
        Iterable<ResourceSource> sources = loadResourcesFromStore(subject);
        if (sources == null) {
            // Load from the internet if we can't for some reason.
            sources = loadResourcesFromClients(subject);
        }
        return sources;
    }

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type from the {@link Client}s
     * of the {@link Explorer}.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T> Type of {@link Examination}.
     * @return {@link SubjectSource}s; can return null under certain circumstances.
     * @throws ParseException Error while parsing data.
     * @throws DownloadException Error while downloading data.
     */
    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromClients(Class<T> examinationClass) throws ParseException, DownloadException {
        // Merge the result from the different getClients into a single iterable.
        List<SubjectSource> sources = new ArrayList<>(64);

        for (Client client : explorer().getClients()) {
            Iterable<SubjectSource> subjectSources = client.getSubjects(examinationClass);

            // Client does not support the examination kind.
            if (subjectSources == null) {
                continue;
            }

            for (SubjectSource source : subjectSources) {
                sources.add(source);
            }
        }

        return sources;
    }

    /**
     * Loads the {@link SubjectSource}s for the specified {@link Examination} type from the {@link Store}
     * of the {@link Explorer}.
     *
     * @param examinationClass {@link Examination} class.
     * @param <T> Type of {@link Examination}.
     * @return {@link SubjectSource}s; can return null under certain circumstances.
     * @throws Exception Exception when loading the {@link SubjectSource}s.
     */
    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromStore(Class<T> examinationClass) throws Exception {
        return explorer().getStore().loadSubjects(examinationClass);
    }

    /**
     * Loads the {@link ResourceSource}s for the specified {@link Subject} instance from the
     *
     * @param subject {@link Subject} instance.
     * @return {@link ResourceSource}s; can return null under certain circumstances.
     * @throws ParseException Error while parsing data.
     * @throws DownloadException Error while downloading data.
     */
    protected Iterable<ResourceSource> loadResourcesFromClients(Subject subject) throws ParseException, DownloadException {
        // Merge the result from the different getClients into a single iterable.
        List<ResourceSource> sources = new ArrayList<>(64);
        Iterable<SubjectSource> subjectSources = subject.sources();

        // Iterate through the list of getSources we have, then get the resource getSources
        // from the corresponding provider.
        for (SubjectSource subjectSource : subjectSources) {
            Iterable<ResourceSource> resourceSources = subjectSource.getClient().getResources(subjectSource);

            // Client does not support the examination kind.
            if (resourceSources == null) {
                continue;
            }

            for (ResourceSource source : resourceSources) {
                sources.add(source);
            }
        }

        return sources;
    }

    /**
     * Loads the {@link ResourceSource}s for the specified {@link Subject} instance from the {@link Store}
     * of the {@link Explorer}.
     *
     * @param subject {@link Subject} instance.
     * @return {@link ResourceSource}s; can return null under certain circumstances.
     * @throws Exception Exception  when loading the {@link ResourceSource}s.
     */
    protected Iterable<ResourceSource> loadResourcesFromStore(Subject subject) throws Exception {
        Iterator<ResourceSource> iterator;
        Iterable<ResourceSource> sources = explorer().getStore().loadResources(subject);
        if (sources == null) {
            return null;
        }

        iterator = sources.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        // Check if the first source is expired, if it
        // is, we return null to indicate we want to load from the getClients.
        if (hasExpired(iterator.next().getDate())) {
            return null;
        }

        return sources;
    }

    /**
     * Determines if the {@link Date} specified is considered expired.
     * @param date {@link Date} to check.
     * @return true if expired; otherwise false.t
     */
    protected boolean hasExpired(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, getDaysBeforeExpiration());
        Date now = new Date();
        Date expire = calendar.getTime();

        return now.after(expire);
    }
}
