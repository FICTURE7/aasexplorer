package com.ficture7.aasexplorer.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResourceTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor_nullName_exception() {
        new Resource(null);
    }

    @Test
    public void name_returns_initializedName() {
        Resource resource = new Resource("test.pdf");

        assertEquals("test.pdf", resource.getName());
    }

    @Test
    public void sources_returns_nonNull() {
        Resource resource = new Resource("test.pdf");

        assertNotNull(resource.getSources());
    }
}