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
        assertNotNull(explorer.clients());
    }

    @Test
    public void examinations_returnNonNull() {
        assertNotNull(explorer.examinations());
    }

    @Test
    public void alevel_notFound_returnsNull() {
        assertNull(explorer.alevel());
    }

    @Test
    public void alevel_found_returnsInstance() throws ExplorerBuilderException {
        //explorer.examinations().add(ALevelExamination.class);
        explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .withExamination(ALevelExamination.class)
                .build();

        assertNotNull(explorer.alevel());
        assertSame(ALevelExamination.class, explorer.alevel().getClass());
    }

    @Test
    public void olevel_notFound_returnsNull() {
        assertNull(explorer.olevel());
    }

    @Test
    public void olevel_found_returnsInstance() throws ExplorerBuilderException {
        explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .withExamination(OLevelExamination.class)
                .build();

        assertNotNull(explorer.olevel());
        assertSame(OLevelExamination.class, explorer.olevel().getClass());
    }
}