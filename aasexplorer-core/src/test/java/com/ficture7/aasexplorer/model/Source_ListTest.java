package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class Source_ListTest {

    private Source.List<Source> sources;

    @Before
    public void setUp() {
        sources = new Source.List<>();
    }

    @Test(expected = NullPointerException.class)
    public void add__null_source__throwsException() {
        sources.add(null);
    }

    @Test
    public void add__size_increases() {
        assertEquals(0, sources.size());
        sources.add(mock(Source.class));

        assertEquals(1, sources.size());
    }

    @Test(expected = IllegalStateException.class)
    public void add__already_contains_source_with_client_of_same_type_as_argument__throwsException() {
        Source source1 = mock(Source.class);
        Source source2 = mock(Source.class);

        when(source1.client()).thenReturn(mock(Client.class));
        when(source2.client()).thenReturn(mock(Client.class));

        sources.add(source1);
        sources.add(source2);
    }

    @Test(expected = NullPointerException.class)
    public void remove__null_source__throwsException() {
        sources.remove(null);
    }

    @Test
    public void remove__size_decreases() {
        Source source = mock(Source.class);
        sources.add(source);

        assertEquals(1, sources.size());

        sources.remove(source);

        assertEquals(0, sources.size());
    }

    @Test
    public void clear__list_cleared() {
        Source source = mock(Source.class);
        sources.add(source);

        assertEquals(1, sources.size());

        sources.clear();

        assertEquals(0, sources.size());
    }

    @Test
    public void iterator__returns_iterator_through_list() {
        Source source = mock(Source.class);

        sources.add(source);

        int count = 0;
        for (Source s : sources) {
            assertSame(source, s);
            count++;
        }

        assertEquals(1, count);
    }
}