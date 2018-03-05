package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.client.Client;

import org.junit.Test;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ResourceSourceTest {

    @Test(expected =  NullPointerException.class)
    public void ctor__client_null__throwsException() {
        new ResourceSource(null, "a", new Date(), URI.create("http://test.com/"));
    }

    @Test(expected =  NullPointerException.class)
    public void ctor__name_null__throwsException() {
        new ResourceSource(mock(Client.class), null, new Date(), URI.create("http://test.com/"));
    }

    @Test(expected =  NullPointerException.class)
    public void ctor__date_null__throwsException() {
        new ResourceSource(mock(Client.class), "a", null, URI.create("http://test.com/"));
    }

    @Test(expected =  NullPointerException.class)
    public void ctor__uri_null__throwsException() {
        new ResourceSource(mock(Client.class), "a", new Date(), null);
    }

    @Test
    public void name__returns_initialized_name() {
        Client client = mock(Client.class);
        String name = "a";
        Date date = new Date();
        URI uri = URI.create("http://test.com/");

        ResourceSource source = new ResourceSource(client, name, date, uri);

        assertEquals(name, source.getName());
    }

    @Test
    public void client__returns_initialized_name() {
        Client client = mock(Client.class);
        String name = "a";
        Date date = new Date();
        URI uri = URI.create("http://test.com/");

        ResourceSource source = new ResourceSource(client, name, date, uri);

        assertEquals(client, source.getClient());
    }

    @Test
    public void date__returns_initialized_name() {
        Client client = mock(Client.class);
        String name = "a";
        Date date = new Date();
        URI uri = URI.create("http://test.com/");

        ResourceSource source = new ResourceSource(client, name, date, uri);

        assertEquals(date, source.getDate());
    }

    @Test
    public void uri__returns_initialized_name() {
        Client client = mock(Client.class);
        String name = "a";
        Date date = new Date();
        URI uri = URI.create("http://test.com/");

        ResourceSource source = new ResourceSource(client, name, date, uri);

        assertEquals(uri, source.getURI());
    }
}
