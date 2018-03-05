package com.ficture7.aasexplorer;

import com.ficture7.aasexplorer.model.examination.Examination;
import com.ficture7.aasexplorer.store.MockStore;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class Explorer_ExaminationsTest {

    private Explorer.Examinations examinations;

    @Before
    public void setUp() throws ExplorerBuilderException {
        Explorer explorer = new ExplorerBuilder()
                .useStore(MockStore.class)
                .build();

        examinations = explorer.getExaminations();
    }

    @Test(expected = NullPointerException.class)
    public void add__examination_null__throwsException() {
        examinations.add(null);
    }

    @Test
    public void add_sizeIncreases() {
        examinations.add(mock(Examination.class));
        assertEquals(1, examinations.size());
    }

    @Test(expected = IllegalStateException.class)
    public void add__already_contains_examination_of_same_type__exception() {
        Examination examination = mock(Examination.class);
        examinations.add(examination);
        examinations.add(examination);
    }

    @Test
    public void get_returns_sameInstance() {
        Examination examination = mock(Examination.class);
        examinations.add(examination);

        Examination e = examinations.get(examination.getClass());
        assertSame(e, examination);
    }

    @Test
    public void get__not_found__returnsNull() {
        assertNull(examinations.get(MockExamination.class));
    }

    @Test
    public void iterator_iterates_through_examinationInstances() {
        Examination examination = mock(Examination.class);
        examinations.add(examination);

        int count = 0;
        for (Examination examination1 : examinations) {
            assertSame(examination, examination1);
            count++;
        }

        assertEquals(1, count);
    }

    private static class MockExamination extends Examination {

        public MockExamination(Loader loader, Saver saver) {
            super(loader, saver);
        }

        @Override
        public String name() {
            return "mock";
        }

        @NotNull
        @Override
        public String getName() {
            return "mock";
        }
    }
}