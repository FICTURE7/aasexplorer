package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
     * Returns the {@link Explorer} instance which owns the {@link ExplorerLoader}.
     *
     * @return {@link Explorer} instance which owns the {@link ExplorerLoader}.
     */
    protected Explorer getExplorer() {
        return explorer;
    }

    /**
     * Returns the number of days before stored data is considered expired.
     *
     * @return Number of days before stored data is considered expired.
     */
    public int getDaysBeforeExpiration() {
        return daysBeforeExpiration;
    }

    /**
     * Sets the number of days before stored data is considered expired.
     *
     * @param days Number of days before data is considered expired.
     */
    public void setDaysBeforeExpiration(int days) {
        daysBeforeExpiration = days;
    }

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
            // Load from the internet if the sources are empty for some reason.
            sources = loadSubjectsFromClients(examinationClass);
        } else if (hasExpired(iterator.next().date())) {
            // Load from the internet if the stored data is considered expired.
            sources = loadSubjectsFromClients(examinationClass);
        }
        return sources;
    }

    @Override
    public <T extends Examination> Iterable<ResourceSource> loadResources(Class<T> examinationClass, Subject subject) throws Exception {
        checkNotNull(examinationClass, "examinationClass");
        checkNotNull(subject, "subject");

        // Try to load from disk first.
        Iterable<ResourceSource> sources = loadResourcesFromStore(examinationClass, subject);
        if (sources == null) {
            // Load from the internet if we can't for some reason.
            sources = loadResourcesFromClients(examinationClass, subject);
        }
        return sources;
    }

    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromClients(Class<T> examinationClass) throws ParseException, DownloadException {
        // Merge the result from the different clients into a single iterable.
        List<SubjectSource> sources = new ArrayList<>(64);

        for (Client client : getExplorer().clients()) {
            //TODO: More defensive loading mechanism.
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

    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromStore(Class<T> examinationClass) throws Exception {
        return getExplorer().store().loadSubjects(examinationClass);
    }

    protected <T extends Examination> Iterable<ResourceSource> loadResourcesFromClients(Class<T> examinationClass, Subject subject) throws Exception {
        // Merge the result from the different clients into a single iterable.
        List<ResourceSource> sources = new ArrayList<>(64);
        Iterable<SubjectSource> subjectSources = subject.sources();

        // Iterate through the list of sources we have, then get the resource sources
        // from the corresponding provider.
        for (SubjectSource subjectSource : subjectSources) {
            //TODO: More defensive loading mechanism.
            Iterable<ResourceSource> resourceSources = subjectSource.client().getResources(subjectSource);

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

    protected <T extends Examination> Iterable<ResourceSource> loadResourcesFromStore(Class<T> examinationClass, Subject subject) throws Exception {
        Iterator<ResourceSource> iterator;
        Iterable<ResourceSource> sources = getExplorer().store().loadResources(examinationClass, subject);
        if (sources == null) {
            return null;
        }

        iterator = sources.iterator();
        if (!iterator.hasNext()) {
            return null;
        }

        // Check if the first source is expired, if it
        // is, we return null to indicate we want to load from the clients.
        if (hasExpired(iterator.next().date())) {
            return null;
        }

        return sources;
    }

    protected boolean hasExpired(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, getDaysBeforeExpiration());
        Date now = new Date();
        Date expire = calendar.getTime();

        return now.after(expire);
    }
}
