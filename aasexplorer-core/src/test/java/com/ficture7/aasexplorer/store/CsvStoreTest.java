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
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class CsvStoreTest {

    @Test(expected = NullPointerException.class)
    public void ctor__fileName_null__throwsException() {
        new CsvStore(null);
    }


    @Test(expected = NullPointerException.class)
    public void configure__null_name__throwsException() throws ExplorerBuilderException {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure(null);
    }

    @Test
    public void name__returns_ame() throws ExplorerBuilderException {
        CsvStore store = new CsvStore(mock(Explorer.class));
        store.configure("test");

        assertEquals("test", store.name());
    }

    @Test
    public void loadSubjects__loads_stored_subjects__returns_subject_sources() throws Exception {
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

    public static class MockExamination extends Examination {

        public MockExamination(Loader loader, Saver saver) {
            super(loader, saver);
        }

        @Override
        public String name() {
            return null;
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