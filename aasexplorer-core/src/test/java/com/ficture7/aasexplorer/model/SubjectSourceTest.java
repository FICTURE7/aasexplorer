package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class SubjectSourceTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_client__throwsException() {
        new SubjectSource(null, 1, "a", new Date(), URI.create("http://test.com/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_name__throwsException() {
        new SubjectSource(mock(Client.class), 1, null, new Date(), URI.create("http://test.com/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_uri__throwsException() {
        new SubjectSource(mock(Client.class), 1, "a", new Date(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctor__null_date__throwsException() {
        new SubjectSource(mock(Client.class), 1, "a", null, URI.create("http://test.com/"));
    }

    @Test
    public void client__returns_initialized_client() {
        Client client = mock(Client.class);
        URI uri = URI.create("http://test.com/");
        SubjectSource source = new SubjectSource(client, 1, "a", new Date(), uri);

        assertSame(client, source.getClient());
    }

    @Test
    public void id__returns_initialized_id() {
        Client client = mock(Client.class);
        URI uri = URI.create("http://test.com/");
        SubjectSource source = new SubjectSource(client, 1, "a", new Date(), uri);

        assertEquals(1, source.getId());
    }

    @Test
    public void name__returns_initialized_name() {
        Client client = mock(Client.class);
        URI uri = URI.create("http://test.com/");
        SubjectSource source = new SubjectSource(client, 1, "a", new Date(), uri);

        assertEquals("a", source.getName());
    }

    @Test
    public void uri__returns_initialized_uri() {
        Client client = mock(Client.class);
        URI uri = URI.create("http://test.com/");
        SubjectSource source = new SubjectSource(client, 1, "a", new Date(), uri);

        assertEquals(uri, source.getURI());
    }
}