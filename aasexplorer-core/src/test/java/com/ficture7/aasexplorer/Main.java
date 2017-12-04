package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.GceGuideClient;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.OLevelExamination;
import com.ficture7.aasexplorer.store.CsvStore;

public class Main {

    public static void main(String[] args) throws Exception {
        Explorer explorer = new ExplorerBuilder()
                .useStore(CsvStore.class, new ExplorerBuilder.Initializer<CsvStore>() {
                    @Override
                    public void init(CsvStore instance) {
                        instance.configure("main");
                    }
                })
                .useLoader(ExplorerLoader.class)
                .useSaver(ExplorerSaver.class)
                .withClient(GceGuideClient.class)
                .withExamination(ALevelExamination.class)
                .withExamination(OLevelExamination.class)
                .build();

        explorer.alevel().subjects().load();
        explorer.alevel().subjects().save();

        Subject physics = explorer.alevel().subjects().get(9702);
        System.out.println(physics);

        physics.resources().load();
        physics.resources().save();

        /*
        physics.resources().load();

        for (Resource resource : physics.resources().questionPapers()) {
            System.out.println(resource.name());
        }
        */
    }
}
