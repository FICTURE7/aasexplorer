package com.ficture7.aasexplorer;

import android.os.AsyncTask;
import android.view.View;

import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.util.LooperUtil;
import com.ficture7.aasexplorer.view.LoaderView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.ficture7.aasexplorer.util.ObjectUtil.checkNotNull;

public class AppExplorerLoader extends ExplorerLoader {

    private final Map<Subject, LoadResourcesAsyncTask> loadResourcesAsyncTaskMap;
    private LoadSubjectsAsyncTask loadSubjectsAsyncTask;

    private boolean autoSave;

    private Status status;
    private LoaderView loaderView;

    //TODO: Add ability to force load from clients.

    public AppExplorerLoader(Explorer explorer) {
        super(explorer);

        status = Status.IDLE;
        loadResourcesAsyncTaskMap = new HashMap<>();
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        setStatus(status, null);
    }

    private void setStatus(Status status, Exception exception) {
        updateLoaderView(status, exception);
    }

    private void setError(Exception e) {
        if (e == null) {
            getLoaderView().getProgressView().setVisibility(View.VISIBLE);
            getLoaderView().getErrorView().setVisibility(View.GONE);
        } else {
            getLoaderView().getProgressView().setVisibility(View.GONE);
            getLoaderView().getErrorView().setVisibility(View.VISIBLE);

            String message;
            if (e instanceof DownloadException) {
                message = "Error downloading data";
            } else if (e instanceof ParseException) {
                message = "Error parsing data";
            } else {
                message = "Unknown error";
            }

            switch (getStatus()) {
                case LOADING_SUBJECTS_FROM_STORE:
                    message += " when loading subjects from local store.";
                    break;
                case LOADING_SUBJECTS_FROM_CLIENTS:
                    message += " when loading subjects from internet.";
                    break;
                case LOADING_RESOURCES_FROM_STORE:
                    message += " when loading resources from local store.";
                    break;
                case LOADING_RESOURCES_FROM_CLIENTS:
                    message += " when loading resources from internet.";
                    break;
                default:
                    message += ".";
                    break;
            }

            getLoaderView().getErrorView().setSubtitle(message);
        }
    }

    public boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean value) {
        autoSave = value;
    }

    public LoaderView getLoaderView() {
        return loaderView;
    }

    public void setLoaderView(LoaderView view) {
        // The hide previous LoaderView if we had one.
        if (loaderView != null) {
            loaderView.setVisibility(View.GONE);
        }

        loaderView = view;
        // Force an update on the new LoaderView instance.
        updateLoaderView(getStatus(), null);
    }

    public LoadSubjectsAsyncTask getLoadSubjectsAsyncTask() {
        if (loadSubjectsAsyncTask == null) {
            loadSubjectsAsyncTask = new LoadSubjectsAsyncTask();
        }

        return loadSubjectsAsyncTask;
    }

    public LoadResourcesAsyncTask getLoadResourcesAsyncTask(Subject subject) {
        // Look up if we already have a load resource task for this subject.
        LoadResourcesAsyncTask task = loadResourcesAsyncTaskMap.get(subject);
        if (task == null) {
            task = new LoadResourcesAsyncTask(subject);
            loadResourcesAsyncTaskMap.put(subject, task);
        }

        return task;
    }

    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        checkNotNull(examinationClass, "examinationClass");
        setStatus(Status.LOADING_SUBJECTS_FROM_STORE);

        // Try to load from disk first.
        Iterable<SubjectSource> sources = loadSubjectsFromStore(examinationClass);
        if (sources == null) {
            setStatus(Status.LOADING_SUBJECTS_FROM_CLIENTS);
            // Load from the internet if we can't for some reason.
            return loadSubjectsFromClients(examinationClass);
        }

        Iterator<SubjectSource> iterator = sources.iterator();
        boolean empty = !iterator.hasNext();
        if (empty || hasExpired(iterator.next().date())) {
            setStatus(Status.LOADING_SUBJECTS_FROM_CLIENTS);

            // Try to load from the internet if the sources are empty for some reason or
            // if stored data is considered expired.
            try {
                sources = loadSubjectsFromClients(examinationClass);
            } catch (Exception e) {
                // If the data loaded from disk was empty and loading from the internet
                // threw an exception, treat as error.
                if (empty) {
                    throw e;
                }
            }
        }
        return sources;
    }

    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        checkNotNull(subject, "subject");
        setStatus(Status.LOADING_RESOURCES_FROM_STORE);

        //NOTE: Use the same loading strategy as loadSubjects().

        Iterable<ResourceSource> sources = loadResourcesFromStore(subject);
        if (sources == null) {
            setStatus(Status.LOADING_RESOURCES_FROM_CLIENTS);
            return loadResourcesFromClients(subject);
        }

        Iterator<ResourceSource> iterator = sources.iterator();
        boolean empty = !iterator.hasNext();
        if (empty || hasExpired(iterator.next().date())) {
            setStatus(Status.LOADING_RESOURCES_FROM_CLIENTS);

            try {
                sources = loadResourcesFromClients(subject);
            } catch (Exception e) {
                if (empty) {
                    throw e;
                }
            }
        }
        return sources;
    }

    @Override
    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromClients(Class<T> examinationClass) throws ParseException, DownloadException {
        Iterable<SubjectSource> sources = super.loadSubjectsFromClients(examinationClass);

        // If auto-save is enabled we starting saving the newly retrieved sources.
        if (sources != null && getAutoSave()) {
            App.getInstance().getSaver().getSaveSubjectsAsyncTask().execute();
        }
        return sources;
    }

    @Override
    protected Iterable<ResourceSource> loadResourcesFromClients(Subject subject) throws ParseException, DownloadException {
        Iterable<ResourceSource> sources = super.loadResourcesFromClients(subject);

        // If auto-save is enabled we starting saving the newly retrieved sources.
        if (sources != null && getAutoSave()) {
            App.getInstance().getSaver().getSaveResourcesAsyncTask(subject).execute();
        }
        return sources;
    }

    // Updates the values of the getLoaderView() instance based on the value
    // of getStatus().
    private void updateLoaderView(Status status, Exception exception) {
        // Make sure we have the LoaderView instance first.
        if (getLoaderView() == null) {
            return;
        }

        // Make sure we run the code on the UI thread.
        LooperUtil.runOnUiThread(new UpdateLoaderViewRunnable(status, exception));
    }

    public enum Status {
        IDLE,

        LOADING_SUBJECTS_FROM_STORE,
        LOADING_SUBJECTS_FROM_CLIENTS,

        LOADING_RESOURCES_FROM_STORE,
        LOADING_RESOURCES_FROM_CLIENTS,
    }

    public class LoadSubjectsAsyncTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;
        private LoadCallback loadCallback;

        @Override
        protected Void doInBackground(Void... args) {
            try {
                getExplorer().alevel().subjects().load();
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            fireLoadCallback();
        }

        public void setLoadCallback(LoadCallback loadCallback) {
            this.loadCallback = loadCallback;

            // If somehow the task completed before we managed to set the callback
            // we fire the callback immediately.
            if (getStatus() == Status.FINISHED) {
                fireLoadCallback();
            }
        }

        private void fireLoadCallback() {
            if (exception == null && loadCallback != null) {
                loadCallback.onLoad();
            }

            setStatus(AppExplorerLoader.Status.IDLE, exception);
        }
    }

    public class LoadResourcesAsyncTask extends AsyncTask<Void, Void, Void> {

        public LoadResourcesAsyncTask(Subject subject) {
            this.subject = subject;
        }

        private Exception exception;
        private Subject subject;

        @Override
        protected Void doInBackground(Void... args) {
            try {
                subject.resources().load();
            } catch (Exception e) {
                exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            setStatus(AppExplorerLoader.Status.IDLE, exception);
        }
    }

    public static abstract class LoadCallback {
        public abstract void onLoad();
    }

    private class UpdateLoaderViewRunnable implements Runnable {

        private final Status status;
        private final Exception exception;

        public UpdateLoaderViewRunnable(Status status, Exception exception) {
            this.status = status;
            this.exception = exception;
        }

        @Override
        public void run() {
            if (exception != null) {
                setError(exception);
                AppExplorerLoader.this.status = status;
            } else {
                AppExplorerLoader.this.status = status;
                if (getStatus() == Status.IDLE) {
                    // Hide the LoaderView when we're not doing anything.
                    getLoaderView().setVisibility(View.GONE);
                } else {
                    getLoaderView().setVisibility(View.VISIBLE);
                }

                switch (getStatus()) {
                    case LOADING_SUBJECTS_FROM_STORE:
                        getLoaderView().getProgressView().setMessage(R.string.message_loading_subjects_from_store);
                        break;
                    case LOADING_SUBJECTS_FROM_CLIENTS:
                        getLoaderView().getProgressView().setMessage(R.string.message_loading_subjects_from_internet);
                        break;
                    case LOADING_RESOURCES_FROM_STORE:
                        getLoaderView().getProgressView().setMessage(R.string.message_loading_resources_from_store);
                        break;
                    case LOADING_RESOURCES_FROM_CLIENTS:
                        getLoaderView().getProgressView().setMessage(R.string.message_loading_resources_from_internet);
                        break;
                }
            }
        }
    }
}
