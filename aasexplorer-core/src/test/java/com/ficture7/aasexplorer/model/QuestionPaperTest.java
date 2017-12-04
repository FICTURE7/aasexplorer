package com.ficture7.aasexplorer.model;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class QuestionPaperTest {

    @Test(expected = NullPointerException.class)
    public void ctor_sessionNull_throwsException() {
        new QuestionPaper("A", null);
    }

    @Test
    public void session_returnsInitializedSession() {
        Session session = new Session(Session.Season.SUMMER, 10);
        QuestionPaper qp = new QuestionPaper("a", session);

        assertSame(session, qp.session());
    }
}