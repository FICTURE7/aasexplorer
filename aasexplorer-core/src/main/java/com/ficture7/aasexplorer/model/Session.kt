package com.ficture7.aasexplorer.model

import com.ficture7.aasexplorer.util.ObjectUtil

/**
 * Represents a getSession.
 *
 * @author FICTURE7
 */
class Session {

    /**
     * Constructs a new instance of the [Session] class with the specified
     * season and year.
     *
     * @param season Season of session.
     * @param year   Year of session.
     */
    constructor(season: Season, year: Int) {
        this.season = season
        this.year = year
    }

    /**
     * Gets the season of the [Session].
     *
     * @return Season of the [Session].
     */
    val season: Season

    /**
     * Gets the year of the [Session].
     *
     * @return Year of the [Session].
     */
    val year: Int

    /**
     * Represents seasons.
     */
    enum class Season {
        /**
         * Unknown getSeason.
         */
        UNKNOWN,

        /**
         * Summer getSeason - May/June.
         */
        SUMMER,

        /**
         * Winter getSeason - November/October.
         */
        WINTER
    }

    companion object {

        /**
         * Parses the specified getSession string.
         *
         * @param value Session string.
         * @return [Session] instance representing the specified session string.
         * @throws IllegalArgumentException `value` is null or is not 3 characters long.
         */
        fun parse(value: String): Session {
            ObjectUtil.checkNotNull(value, "value")

            if (value.length != 3) {
                throw IllegalArgumentException("value must 3 characters long.")
            }

            var season = Season.UNKNOWN
            val c = value[0]
            when (c) {
                's' -> season = Season.SUMMER
                'w' -> season = Season.WINTER
            }

            val yearStr = value.substring(1)
            val year = 2000 + Integer.parseInt(yearStr)
            return Session(season, year)
        }
    }
}
