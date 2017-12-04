package com.ficture7.aasexplorer;

import android.os.AsyncTask;

import com.ficture7.aasexplorer.model.Subject;

import java.util.HashMap;
import java.util.Map;

public class AppExplorerSaver extends ExplorerSaver {

    private Map<Subject, SaveResourcesAsyncTask> saveResourcesAsyncTaskMap;
    private SaveSubjectsAsyncTask saveSubjectsAsyncTask;

    public AppExplorerSaver(Explorer explorer) {
        super(explorer);

        saveResourcesAsyncTaskMap = new HashMap<>();
    }

    public SaveSubjectsAsyncTask getSaveSubjectsAsyncTask() {
        if (saveSubjectsAsyncTask == null) {
            saveSubjectsAsyncTask = new SaveSubjectsAsyncTask();
        }
        return saveSubjectsAsyncTask;
    }

    public SaveResourcesAsyncTask getSaveResourcesAsyncTask(Subject subject) {
        SaveResourcesAsyncTask task = saveResourcesAsyncTaskMap.get(subject);
        if (task == null) {
            task = new SaveResourcesAsyncTask(subject);
            saveResourcesAsyncTaskMap.put(subject, task);
        }

        return task;
    }

    public class SaveSubjectsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            try {
                explorer().alevel().subjects().save();
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    public class SaveResourcesAsyncTask extends AsyncTask<Void, Void, Void> {

        private Subject subject;

        public SaveResourcesAsyncTask(Subject subject) {
            this.subject = subject;
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                subject.resources().save();
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }
}
