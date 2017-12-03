package com.ficture7.aasexplorer.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionTest {

    @Test(expected = NullPointerException.class)
    public void ctor__season_null__throwsException() {
        new Session(null, 2017);
    }

    @Test
    public void season_returnsSeason() {
        Session session = new Session(Session.Season.SUMMER, 2017);
        assertEquals(Session.Season.SUMMER, session.season());
    }

    @Test
    public void year_returnsYear() {
        Session session = new Session(Session.Season.SUMMER, 2017);
        assertEquals(2017, session.year());
    }

    @Test(expected = NullPointerException.class)
    public void parse_nullValue_exception() {
        Session.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_valueLength_lessThan3_exception() {
        Session.parse("w1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_valueLength_greaterThan3_exception() {
        Session.parse("w111");
    }

    @Test
    public void parse_winterSeason_returnsWinter_returnsYear() {
        Session session = Session.parse("w07");
        assertEquals(Session.Season.WINTER, session.season());
        assertEquals(2007, session.year());
    }

    @Test
    public void parse_summerSeason_returnsSummer_returnsYear() {
        Session session = Session.parse("s11");
        assertEquals(Session.Season.SUMMER, session.season());
        assertEquals(2011, session.year());
    }

    @Test
    public void parse_unknownSeason_returnsUnknown_returnsYear() {
        Session session = Session.parse("x17");
        assertEquals(Session.Season.UNKNOWN, session.season());
        assertEquals(2017, session.year());
    }

    @Test(expected = NumberFormatException.class)
    public void parse_invalidYear_exception() {
        Session.parse("wxx");
    }
}