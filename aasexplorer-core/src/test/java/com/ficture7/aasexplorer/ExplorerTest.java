package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.examination.ALevelExamination;
import com.ficture7.aasexplorer.model.examination.OLevelExamination;
import com.ficture7.aasexplorer.store.MockStore;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ExplorerTest {

    private Explorer explorer;

    @Before
    public void setUp() throws ExplorerBuilderException {
        explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .build();
    }

    @Test
    public void clients_returnsNonNull() {
        assertNotNull(explorer.getClients());
    }

    @Test
    public void examinations_returnNonNull() {
        assertNotNull(explorer.getExaminations());
    }

    @Test
    public void alevel_notFound_returnsNull() {
        assertNull(explorer.getALevel());
    }

    @Test
    public void alevel_found_returnsInstance() throws ExplorerBuilderException {
        //explorer.getExaminations().add(ALevelExamination.class);
        explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .withExamination(ALevelExamination.class)
                .build();

        assertNotNull(explorer.getALevel());
        assertSame(ALevelExamination.class, explorer.getALevel().getClass());
    }

    @Test
    public void olevel_notFound_returnsNull() {
        assertNull(explorer.getOLevel());
    }

    @Test
    public void olevel_found_returnsInstance() throws ExplorerBuilderException {
        explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .withExamination(OLevelExamination.class)
                .build();

        assertNotNull(explorer.getOLevel());
        assertSame(OLevelExamination.class, explorer.getOLevel().getClass());
    }
}