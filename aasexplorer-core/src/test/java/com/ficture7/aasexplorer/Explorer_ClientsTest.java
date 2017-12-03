package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.store.MockStore;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

public class Explorer_ClientsTest {
    private Explorer.Clients clients;

    @Before
    public void setUp() throws ExplorerBuilderException {
        Explorer explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .build();

        clients = explorer.clients();
    }

    @Test(expected = NullPointerException.class)
    public void add_nullClass_exception() {
        clients.add(null);
    }

    @Test
    public void add_sizeIncreases() {

        Client client = new MockClientB();
        clients.add(client);

        assertEquals(1, clients.size());

        Client rclient = clients.get(MockClientB.class);
        assertSame(client, rclient);
    }

    @Test(expected = IllegalStateException.class)
    public void add_duplicateType_exception() {
        clients.add(new MockClientB());
        clients.add(new MockClientB());
    }

    @Test
    public void maps_clientClass_to_client_instances() {
        Client clientB = new MockClientB();
        Client clientA = new MockClientA();

        clients.add(clientB);
        clients.add(clientA);

        assertEquals(2, clients.size());

        Client rclientB = clients.get(MockClientB.class);
        Client rclientC = clients.get(MockClientA.class);
        assertSame(clientB, rclientB);
        assertSame(clientA, rclientC);
    }

    @Test
    public void get__client_not_found__returnsNull() {
        MockClientA client = clients.get(MockClientA.class);
        assertNull(client);
    }

    @Test(expected = NullPointerException.class)
    public void get__clientClass_null__throwsException(){
        clients.get(null);
    }

    @Test
    public void iterator_iterates_through_clientInstances() {
        maps_clientClass_to_client_instances();

        int count = 0;
        for (Client client : clients) {
            assertNotNull(client);
            count++;
        }

        assertEquals(2, count);
    }

    private static class MockClientA extends AbstractMockClient {
        public MockClientA() {
            // Space
        }
    }

    private static class MockClientB extends AbstractMockClient {
        public MockClientB() {
            // Space
        }
    }

    private static abstract class AbstractMockClient implements Client {
        @Override
        public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
            return null;
        }

        @Override
        public Iterable<ResourceSource> getResources(SubjectSource subjectSource) {
            return null;
        }
    }
}