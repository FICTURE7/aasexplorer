package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.client.Client;
import com.ficture7.aasexplorer.client.DownloadException;
import com.ficture7.aasexplorer.client.ParseException;
import com.ficture7.aasexplorer.model.ResourceSource;
import com.ficture7.aasexplorer.model.Subject;
import com.ficture7.aasexplorer.model.SubjectSource;
import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.store.Store;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ExplorerBuilderTest {

    private ExplorerBuilder builder;

    @Before
    public void setUp() {
        builder = new ExplorerBuilder();
        builder.useStore(MockStore.class);
    }

    @Test(expected = ExplorerBuilderException.class)
    public void useLoader__no_valid_constructors__throwsException() throws ExplorerBuilderException {
        builder.useLoader(MockLoader_NoValidConstructor.class);
        builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void withExamination__null_examinationClass__throwsException() {
        builder.withExamination(null);
    }

    @Test
    public void withExamination__success__returns_same_builder_instance() {
        assertSame(builder, builder.withExamination(MockExamination.class));
    }

    @Test
    public void withExamination__success__examination_added_to_explorer() throws ExplorerBuilderException {
        builder.withExamination(MockExamination.class);
        Explorer explorer = builder.build();

        assertEquals(1, explorer.getExaminations().size());
        assertNotNull(explorer.getExaminations().get(MockExamination.class));
    }

    @Test(expected = ExplorerBuilderException.class)
    public void withExamination__failed_to_add__throwsException() throws ExplorerBuilderException {
        builder.withExamination(MockExaminationA.class);
        builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void withClient__null_clientClass_throwsException() throws ExplorerBuilderException {
        builder.withClient(null);
    }

    @Test
    public void withClient__success__returns_same_builder_instance() throws ExplorerBuilderException {
        assertSame(builder, builder.withClient(MockClient.class));
    }

    @Test
    public void withClient__success__client_added_to_explorer() throws ExplorerBuilderException {
        builder.withClient(MockClient.class);
        Explorer explorer = builder.build();

        assertEquals(1, explorer.getClients().size());
        assertNotNull(explorer.getClients().get(MockClient.class));
    }

    @Test(expected = ExplorerBuilderException.class)
    public void withClient__failed_to_add__throwsException() throws ExplorerBuilderException {
        builder.withClient(MockClientA.class);
        builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void useStore__null_store__throwsException() throws ExplorerBuilderException {
        builder.useStore(null);
    }

    @Test(expected = ExplorerBuilderException.class)
    public void useStore__failed_to_construct_instance__throwsException() throws ExplorerBuilderException {
        builder.useStore(MockStore_PrivateConstructor.class);
        builder.build();
    }

    @Test
    public void useStore__success__returns_same_builder_instance() throws ExplorerBuilderException {
        assertSame(builder, builder.useStore(MockStore.class));
    }

    @Test
    public void useStore__success__store_set() throws ExplorerBuilderException {
        Explorer explorer = builder.useStore(MockStore.class).build();

        assertNotNull(explorer.getStore());
    }

    @Test
    public void useStore__with_initializer__store_passed_to_initializer() throws ExplorerBuilderException {
        ExplorerBuilder.Initializer<MockStore> initializer = new ExplorerBuilder.Initializer<MockStore>() {
            @Override
            public void init(MockStore instance) {
                assertNotNull(instance);
                assertEquals("defaultValue", instance.test);

                instance.test = "updatedValue";
            }
        };

        builder.useStore(MockStore.class, initializer);

        Explorer explorer = builder.build();
        MockStore store = (MockStore) explorer.getStore();


        assertEquals("updatedValue", store.test);
    }

    @Test
    public void build__returns_non_null() throws ExplorerBuilderException {
        Explorer explorer = builder.build();
        assertNotNull(explorer);
    }

    private static class MockStore_PrivateConstructor extends Store {
        private MockStore_PrivateConstructor(Explorer explorer) {
            super(explorer);
        }

        @Override
        public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {

        }

        @Override
        public void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {

        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
            return null;
        }

        @Override
        public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
            return null;
        }
    }

    private static class MockStore extends Store {

        public String test = "defaultValue";

        public MockStore(Explorer explorer) {
            super(explorer);
        }

        @Override
        public <T extends Examination> void saveSubjects(Class<T> examinationClass, Iterable<SubjectSource> subjectSources) throws Exception {

        }

        @Override
        public void saveResources(Subject subject, Iterable<ResourceSource> resourceSources) throws Exception {

        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> loadSubjects(Class<T> examinationClass) throws Exception {
            return null;
        }

        @Override
        public Iterable<ResourceSource> loadResources(Subject subject) throws Exception {
            return null;
        }
    }

    private static class MockClientA implements Client {

        private MockClientA() {
            // private constructor to cause an error.
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
            return null;
        }

        @Override
        public Iterable<ResourceSource> getResources(SubjectSource subjectSource) {
            return null;
        }
    }

    private static class MockClient implements Client {

        public MockClient() {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public <T extends Examination> Iterable<SubjectSource> getSubjects(Class<T> examinationClass) throws ParseException, DownloadException {
            return null;
        }

        @Override
        public Iterable<ResourceSource> getResources(SubjectSource subjectSource) {
            return null;
        }
    }

    private static class MockExaminationA extends Examination {

        private MockExaminationA(Loader loader, Saver saver) {
            super(loader, saver);

            // private constructor to cause an error.
        }

        @NotNull
        @Override
        public String getName() {
            return "mock which causes a error when Examinations tries to create a new instance.";
        }
    }

    private static class MockExamination extends Examination {
        public MockExamination(Loader loader, Saver saver) {
            super(loader, saver);
        }

        @NotNull
        @Override
        public String getName() {
            return "mock";
        }
    }

    private class MockLoader_NoValidConstructor extends ExplorerLoader {
        public MockLoader_NoValidConstructor(Explorer explorer, int x) {
            super(explorer);
        }
    }
}