package com.ficture7.aasexplorer.model;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class QuestionPaperTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctor_sessionNull_throwsException() {
        new QuestionPaper("A", null, 1);
    }

    @Test
    public void session_returnsInitializedSession() {
        Session session = new Session(Session.Season.SUMMER, 10);
        QuestionPaper qp = new QuestionPaper("a", session, 1);

        assertSame(session, qp.getSession());
    }

    @Test
    public void number_returnsInitializedNumber() {
        Session session = new Session(Session.Season.SUMMER, 10);
        QuestionPaper qp = new QuestionPaper("a", session, 1);

        assertSame(1, qp.getNumber());
    }
}