package com.slobodanantonijevic.simpleopenweather.general;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Just for a bunch of static processing functions that we need on multiple
 * sources but want to keep them mutual
 */
public class HelpStuff {

    /**
     * Since we need the app to run on APIs prior to 26 we can't use java.time
     * but we can use ThreeTen Android Backport, which is quite efficient and
     * has been endorsed by Google in multiple articles
     * https://github.com/JakeWharton/ThreeTenABP
     */
    private static ZonedDateTime zonedDateTime(int unixDate) {

        ZoneId zoneId = ZoneId.systemDefault();

        return Instant.ofEpochMilli(unixDate * 1000L).atZone(zoneId);
    }

    public static String weekDay(int unixDate) {

        return DateTimeFormatter.ofPattern("EEEE").format(zonedDateTime(unixDate));
    }

    public static String date(int unixDate) {

        return DateTimeFormatter.ofPattern("MMMM d").format(zonedDateTime(unixDate));
    }

    public static String roundTheTemp(double temp) {

        // No need for long (Math.round returns long) temperature will never be that high number
        return Integer.toString((int) Math.round(temp));
    }
}
