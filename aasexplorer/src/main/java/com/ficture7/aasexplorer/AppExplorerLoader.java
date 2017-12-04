package com.ficture7.aasexplorer;

import android.os.AsyncTask;
import android.os.Looper;
import android.view.View;

import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.ui.view.LoaderView;

import java.util.HashMap;
import java.util.Map;

public class AppExplorerLoader extends ExplorerLoader {

    private final UpdateLoaderViewRunnable updateLoaderViewRunnable;

    private Map<Subject, LoadResourcesAsyncTask> loadResourcesAsyncTaskMap;
    private LoadSubjectsAsyncTask loadSubjectsAsyncTask;

    private boolean autoSave;

    private Status status;
    private LoaderView loaderView;

    //TODO: Add ability to force load from clients.

    public AppExplorerLoader(Explorer explorer) {
        super(explorer);

        status = Status.IDLE;
        loadResourcesAsyncTaskMap = new HashMap<>();
        updateLoaderViewRunnable = new UpdateLoaderViewRunnable();
    }

    public Status getStatus() {
        return status;
    }

    private void setStatus(Status status) {
        this.status = status;
        updateLoaderView();
    }

    private void setStatusNoUpdate(Status status) {
        this.status = status;
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
        updateLoaderView();
    }

    public LoadSubjectsAsyncTask getLoadSubjectsAsyncTask() {
        if (loadSubjectsAsyncTask == null) {
            loadSubjectsAsyncTask = new LoadSubjectsAsyncTask();
        }

        //TODO: Create new async tasks when the previous one has completed.
        return loadSubjectsAsyncTask;
    }

    public LoadResourcesAsyncTask getLoadResourcesAsyncTask(Subject subject) {
        LoadResourcesAsyncTask task = loadResourcesAsyncTaskMap.get(subject);
        if (task == null) {
            task = new LoadResourcesAsyncTask(subject);
            loadResourcesAsyncTaskMap.put(subject, task);
        }

        //TODO: Create new async tasks when the previous one has completed.
        return task;
    }

    @Override
    public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
        Iterable<SubjectSource> sources =  super.loadSubjects(examinationClass);

        App.getInstance().getEventBus().post("");

        // Check if we're running inside the doInBackground method (kinda).
        // If yes, we don't push an update to the UI yet.
        if (getLoadSubjectsAsyncTask().workerThread != Thread.currentThread()) {
            setStatus(Status.IDLE);
        } else {
            setStatusNoUpdate(Status.IDLE);
        }
        return sources;
    }

    @Override
    public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
        Iterable<ResourceSource> sources =  super.loadResources(subject);

        // Check if we're running inside the doInBackground method (kinda).
        // If yes, we don't push an update to the UI yet.
        if (getLoadResourcesAsyncTask(subject).workerThread != Thread.currentThread()) {
            setStatus(Status.IDLE);
        } else {
            setStatusNoUpdate(Status.IDLE);
        }
        return sources;
    }

    @Override
    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromStore(Class<T> examinationClass) throws Exception {
        setStatus(Status.LOADING_SUBJECTS_FROM_STORE);
        return super.loadSubjectsFromStore(examinationClass);
    }

    @Override
    protected <T extends Examination> Iterable<SubjectSource> loadSubjectsFromClients(Class<T> examinationClass) throws ParseException, DownloadException {
        setStatus(Status.LOADING_SUBJECTS_FROM_CLIENTS);
        Iterable<SubjectSource> sources = super.loadSubjectsFromClients(examinationClass);

        // If auto-save is enabled we starting saving the newly retrieved sources.
        if (sources != null && getAutoSave()) {
            App.getInstance().getSaver().getSaveSubjectsAsyncTask().execute();
        }
        return sources;
    }

    @Override
    protected Iterable<ResourceSource> loadResourcesFromStore(Subject subject) throws Exception {
        setStatus(Status.LOADING_RESOURCES_FROM_STORE);
        return super.loadResourcesFromStore(subject);
    }

    @Override
    protected Iterable<ResourceSource> loadResourcesFromClients(Subject subject) throws Exception {
        setStatus(Status.LOADING_RESOURCES_FROM_CLIENTS);
        Iterable<ResourceSource> sources = super.loadResourcesFromClients(subject);

        // If auto-save is enabled we starting saving the newly retrieved sources.
        if (sources != null && getAutoSave()) {
            App.getInstance().getSaver().getSaveResourcesAsyncTask(subject).execute();
        }
        return sources;
    }

    // Updates the values of the getLoaderView() instance based on the value
    // of getStatus().
    private void updateLoaderView() {
        // Make sure we have the LoaderView instance first.
        if (getLoaderView() == null) {
            return;
        }

        // Make sure we run the code on the UI thread.
        boolean inUiThread = Looper.getMainLooper() == Looper.myLooper();
        if (inUiThread) {
            updateLoaderViewRunnable.run();
        } else {
            loaderView.post(updateLoaderViewRunnable);
        }
    }

    public enum Status {
        IDLE,

        LOADING_SUBJECTS_FROM_STORE,
        LOADING_SUBJECTS_FROM_CLIENTS,

        LOADING_RESOURCES_FROM_STORE,
        LOADING_RESOURCES_FROM_CLIENTS
    }

    public class LoadSubjectsAsyncTask extends AsyncTask<Void, Void, Void> {

        private LoadCallback loadCallback;
        // Thread on which doInBackground method is running.
        private Thread workerThread;

        public void setLoadCallback(LoadCallback loadCallback) {
            this.loadCallback = loadCallback;

            // If somehow the task completed before we managed to set the callback
            // we fire the callback immediately.
            if (getStatus() == Status.FINISHED) {
                fireLoadCallback();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {
            //TODO: Track exception.

            workerThread = Thread.currentThread();

            try {
                getExplorer().alevel().subjects().load();
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            fireLoadCallback();
        }

        private void fireLoadCallback() {
            if (loadCallback != null) {
                // NOTE: Consider making sure running on the UI thread.
                loadCallback.onLoad();
            }
            updateLoaderView();
        }
    }

    public class LoadResourcesAsyncTask extends AsyncTask<Void, Void, Void> {

        public LoadResourcesAsyncTask(Subject subject) {
            this.subject = subject;
        }

        private Subject subject;
        // Thread on which doInBackground method is running.
        private Thread workerThread;

        @Override
        protected Void doInBackground(Void... args) {
            workerThread = Thread.currentThread();

            try {
                subject.resources().load();
            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            updateLoaderView();
        }
    }

    public static abstract class LoadCallback {
        public abstract void onLoad();
    }

    private class UpdateLoaderViewRunnable implements Runnable {

        @Override
        public void run() {
            if (getStatus() == Status.IDLE) {
                // Hide the LoaderView when we're not doing anything.
                getLoaderView().setVisibility(View.GONE);
            } else {
                getLoaderView().setVisibility(View.VISIBLE);
            }

            //TODO: Turn those stuff into strings.xml resources.
            switch (getStatus()) {
                case LOADING_SUBJECTS_FROM_STORE:
                    getLoaderView().getProgressView().setMessage("Loading subjects from local store...");
                    break;
                case LOADING_SUBJECTS_FROM_CLIENTS:
                    getLoaderView().getProgressView().setMessage("Loading subjects from internet...");
                    break;
                case LOADING_RESOURCES_FROM_STORE:
                    getLoaderView().getProgressView().setMessage("Loading resources from local store...");
                    break;
                case LOADING_RESOURCES_FROM_CLIENTS:
                    getLoaderView().getProgressView().setMessage("Loading resources from internet...");
                    break;
            }
        }
    }
}
