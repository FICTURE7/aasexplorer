package com.ficture7.aasexplorer.store;

import com.ficture7.aasexplorer.Explorer;
import com.ficture7.aasexplorer.ExplorerBuilder;
import com.ficture7.aasexplorer.ExplorerBuilderException;
import com.ficture7.aasexplorer.Loader;
import com.ficture7.aasexplorer.Saver;
import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.model.examination.MockExamination;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CsvStoreTest {

    @Test(expected = NullPointerException.class)
    public void ctor__explorer_null__throwsException() {
        new CsvStore(null);
    }

    @Test(expected = NullPointerException.class)
    public void configure__null_directory__throwsException() {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure(null);
    }

    @Test
    public void root__returns_root() {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("test");

        assertEquals("test", store.root().getPath());
    }

    @Test
    public void loadSubjects__files_does_not_exists__returnsNull() throws Exception {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("does-not-exists");

        Iterable<SubjectSource> sources = store.loadSubjects(MockExamination.class);

        assertNull(sources);
    }

    @Test
    public void loadSubjects__empty__returns_null() throws Exception {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("aasexplorer-core/src/test/resources/test-empty");

        Iterable<SubjectSource> sources = store.loadSubjects(MockExamination.class);

        assertNull(sources);
    }

    @Test
    public void loadSubjects__returns_stored_subjects() throws Exception {
        int[] ids = {
                1,
                2,
                4
        };
        String[] names = {
                "xD",
                "LaL",
                "KeK",
        };
        String[] uris = {
                "http://mock.com/xD",
                "http://mock.com/LaL",
                "http://mock.com/KeK",
        };

        Explorer explorer = new ExplorerBuilder()
                .useStore(CsvStore.class, new ExplorerBuilder.Initializer<CsvStore>() {
                    @Override
                    public void init(CsvStore instance) {
                        instance.configure("aasexplorer-core/src/test/resources/test");
                    }
                })
                .withClient(MockClient.class)
                .withExamination(MockExamination.class)
                .build();

        CsvStore store = (CsvStore) explorer.store();

        Iterable<SubjectSource> sources = store.loadSubjects(MockExamination.class);
        assertNotNull(sources);

        int i = 0;
        for (SubjectSource source : sources) {
            assertEquals(source.id(), ids[i]);
            assertEquals(source.name(), names[i]);
            assertEquals(source.uri().toString(), uris[i]);
            assertSame(source.client(), explorer.clients().get(MockClient.class));

            i++;
        }

        assertEquals(ids.length, i);
    }

    @Test
    public void loadResources__files_does_not_exists__returns_null() throws Exception {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("does-not-exists");

        Iterable<ResourceSource> sources = store.loadResources(mock(Subject.class));

        assertNull(sources);
    }

    @Test
    public void loadResources__empty__returns_null() throws Exception {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("aasexplorer-core/src/test/resources/test-empty");

        Subject subject = mock(Subject.class);
        when(subject.examination()).thenReturn(mock(Examination.class));

        Iterable<ResourceSource> sources = store.loadResources(subject);

        assertNull(sources);
    }

    @Test
    public void loadResources__returns_stored_subjects() throws Exception {
        String[] names = {
                "1337_s06_ms_1",
                "1337_s06_ms_2",
                "1337_s06_ms_3",
                "1337_s06_ms_4",
                "1337_s06_ms_5",
                "1337_s06_ms_6",
                "1337_s06_ms_7",
                "1337_s06_ms_8",
                "1337_s06_ms_9",
        };
        String[] urls = {
                "http://mock.com/1/1337_s06_ms_1",
                "http://mock.com/1/1337_s06_ms_2",
                "http://mock.com/1/1337_s06_ms_3",
                "http://mock.com/1/1337_s06_ms_4",
                "http://mock.com/1/1337_s06_ms_5",
                "http://mock.com/1/1337_s06_ms_6",
                "http://mock.com/1/1337_s06_ms_7",
                "http://mock.com/1/1337_s06_ms_8",
                "http://mock.com/1/1337_s06_ms_9",
        };

        Explorer explorer = new ExplorerBuilder()
                .useStore(CsvStore.class, new ExplorerBuilder.Initializer<CsvStore>() {
                    @Override
                    public void init(CsvStore instance) {
                        instance.configure("aasexplorer-core/src/test/resources/test");
                    }
                })
                .withClient(MockClient.class)
                .withExamination(MockExamination.class)
                .build();

        CsvStore store = (CsvStore) explorer.store();

        Subject subject = mock(Subject.class);
        when(subject.examination()).thenReturn(explorer.examinations().get(MockExamination.class));
        when(subject.id()).thenReturn(1);

        Iterable<ResourceSource> sources = store.loadResources(subject);
        assertNotNull(sources);

        int i = 0;
        for (ResourceSource source : sources) {
            assertEquals(names[i], source.name());
            assertEquals(urls[i], source.uri().toString());
            assertEquals(explorer.clients().get(MockClient.class), source.client());
            i++;
        }
    }

    public static class MockClient implements Client {

        public MockClient() {

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
}