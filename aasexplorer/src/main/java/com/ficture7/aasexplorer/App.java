package com.ficture7.aasexplorer;

import android.app.Application;

import com.ficture7.aasexplorer.client.GceGuideClient;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.store.CsvStore;

public class App extends Application {

    private static App instance;

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

    public EventBus getEventBus() {
        return null;
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
                            String path = getApplicationContext().getFilesDir().getAbsolutePath() + "data";

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
            //TODO: More graceful, if somehow it happens, because it should not.
            throw new RuntimeException(e);
        }

        // Start loading the subjects on a separate thread immediately when
        // the application is created.
        getLoader().getLoadSubjectsAsyncTask().execute();
        instance = this;
    }
}