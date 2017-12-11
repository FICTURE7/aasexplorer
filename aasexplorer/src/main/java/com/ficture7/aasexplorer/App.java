package com.ficture7.aasexplorer;

import android.app.Application;

import com.ficture7.aasexplorer.client.GceGuideClient;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.store.CsvStore;

public class App extends Application {

    private static App instance;

    private Navigator navigator;
    private Bookmarks bookmarks;
    private Explorer explorer;
    private AppExplorerLoader explorerLoader;
    private AppExplorerSaver explorerSaver;

    public Explorer getExplorer() {
        return explorer;
    }

    public AppExplorerLoader getLoader() {
        return explorerLoader;
    }

    public AppExplorerSaver getSaver() {
        return explorerSaver;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public Bookmarks getBookmarks() {
        return bookmarks;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Construct a new instance of the Explorer instance.
        try {
            explorer = new ExplorerBuilder()
                    .useStore(CsvStore.class, new ExplorerBuilder.Initializer<CsvStore>() {
                        @Override
                        public void init(CsvStore instance) {
                            // Configure the store to use the internal storage of the device.
                            String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/data";

                            instance.configure(path);
                        }
                    })
                    .useLoader(AppExplorerLoader.class, new ExplorerBuilder.Initializer<AppExplorerLoader>() {
                        @Override
                        public void init(AppExplorerLoader instance) {
                            explorerLoader = instance;
                            // Enable auto save to save every-time something is loaded from the store.
                            explorerLoader.setAutoSave(true);
                            explorerLoader.setDaysBeforeExpiration(7);
                        }
                    })
                    .useSaver(AppExplorerSaver.class, new ExplorerBuilder.Initializer<AppExplorerSaver>() {
                        @Override
                        public void init(AppExplorerSaver instance) {
                            explorerSaver = instance;
                        }
                    })
                    .withClient(GceGuideClient.class)
                    .withExamination(ALevelExamination.class)
                    .build();
        } catch (ExplorerBuilderException e) {
            throw new RuntimeException("Unexpected exception while building Explorer instance.", e);
        }

        // Initialize bookmarks and load bookmarks from disk.
        bookmarks = new Bookmarks(getApplicationContext());
        // Initialize navigator.
        navigator = new Navigator();

        // Start loading the subjects on a separate thread immediately when
        // the application is created.
        getLoader().getLoadSubjectsAsyncTask().execute();

        // Set the singleton instance.
        instance = this;
    }

    public static final class Intents {
        public static final String BASE = "com.ficture7.aasexplorer.";

        public static final String SUBJECT_ID = BASE + "SUBJECT_ID";
    }
}
