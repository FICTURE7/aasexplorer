package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.ExplorerBuilder;
import com.ficture7.aasexplorer.ExplorerBuilderException;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class StoreTest {

    @Test(expected = NullPointerException.class)
    public void ctor__null_explorer__throwsException() {
        new MockStore(null);
    }

    @Test
    public void getClient__class_not_found__returnsNull() throws ExplorerBuilderException {
        Store store = new MockStore(mock(Explorer.class));

        Client client = store.getClient("com.ficture7.aasexplorer.client.ClientWhichDoesNotExists");
        assertNull(client);
    }

    @Test
    public void getClient__client_exists_and_is_in_explorer__returns_client_instance() throws ExplorerBuilderException {
        Explorer explorer = new ExplorerBuilder()
                .withClient(MockClient.class)
                .useStore(MockStore.class)
                .build();

        Store store = explorer.store();

        Client client = store.getClient("com.ficture7.aasexplorer.store.StoreTest$MockClient");
        assertNotNull(client);
        assertThat(client, IsInstanceOf.instanceOf(MockClient.class));
    }

    public static class MockClient implements Client {

        public MockClient() {
            // Space
        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
            return null;
        }

        @Override
        public Iterable<ResourceSource> getResources(SubjectSource subjectSource) throws ParseException, DownloadException {
            return null;
        }
    }

    public static class MockStore extends Store {

        public MockStore(Explorer explorer) {
            super(explorer);
        }

        @Override
        public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {

        }

        @Override
        public <T extends Examination> void saveResources(Class<T> examinationClass, Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {

        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
            return null;
        }

        @Override
        public <T extends Examination> Iterable<ResourceSource> loadResources(Class<T> examinationClass, Subject subject) throws Exception {
            return null;
        }
    }
}