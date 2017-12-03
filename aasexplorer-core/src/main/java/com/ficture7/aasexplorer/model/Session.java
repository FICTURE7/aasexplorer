package com.ficture7.aasexplorer.model;

import com.ficture7.aasexplorer.util.ObjectUtil;

/**
 * Represents a session.
 *
 * @author FICTURE7
 */
public class Session {

    // Season of session.
    private final Season season;
    // Year of session.
    private final int year;

    /**
     * Constructs a new instance of the {@link Session} class with the specified
     * season and year.
     *
     * @param season Season of session.
     * @param year   Year of session.
     * @throws IllegalArgumentException {@code season} is null.
     */
    public Session(Season season, int year) {
        this.season = ObjectUtil.checkNotNull(season, "season");
        this.year = year;
    }

    /**
     * Gets the season of the {@link Session}.
     *
     * @return Season of the {@link Session}.
     */
    public Season season() {
        return season;
    }

    /**
     * Gets the year of the {@link Session}.
     *
     * @return Year of the {@link Session}.
     */
    public int year() {
        return year;
    }

    /**
     * Parses the specified session string.
     *
     * @param value Session string.
     * @return {@link Session} instance representing the specified session string.
     * @throws IllegalArgumentException {@code value} is null or is not 3 characters long.
     */
    public static Session parse(String value) {
        ObjectUtil.checkNotNull(value, "value");

        if (value.length() != 3) {
            throw new IllegalArgumentException("value must 3 characters long.");
        }

        Season season = Season.UNKNOWN;
        char c = value.charAt(0);
        switch (c) {
            case 's':
                season = Season.SUMMER;
                break;
            case 'w':
                season = Season.WINTER;
                break;
        }

        String yearStr = value.substring(1);
        int year = 2000 + Integer.parseInt(yearStr);
        return new Session(season, year);
    }

    /**
     * Represents seasons.
     */
    public enum Season {
        /**
         * Unknown season.
         */
        UNKNOWN,

        /**
         * Summer season - May/June.
         */
        SUMMER,

        /**
         * Winter season - November/October.
         */
        WINTER
    }
}
