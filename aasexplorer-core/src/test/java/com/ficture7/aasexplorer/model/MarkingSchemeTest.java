package com.ficture7.aasexplorer.model;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class MarkingSchemeTest {

    @Test(expected = NullPointerException.class)
    public void ctor_sessionNull_throwsException() {
        new MarkingScheme("A", null);
    }

    @Test
    public void session_returnsInitializedSession() {
        Session session = new Session(Session.Season.SUMMER, 10);
        MarkingScheme ms = new MarkingScheme("a", session);

        assertSame(session, ms.session());
    }
}