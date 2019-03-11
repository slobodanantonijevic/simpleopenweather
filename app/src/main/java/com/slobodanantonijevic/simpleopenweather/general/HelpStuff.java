package com.slobodanantonijevic.simpleopenweather.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.slobodanantonijevic.simpleopenweather.R;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Just for a bunch of static processing functions that we need on multiple
 * sources but want to keep them mutual
 */
public class HelpStuff {

    private static final String BASIC_CONFIG_FILE = "basic_config_file";

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

    /**
     *
     * @param unixDate
     * @return
     */
    public static String weekDay(int unixDate) {

        return DateTimeFormatter.ofPattern("EEEE").format(zonedDateTime(unixDate));
    }

    /**
     *
     * @param unixDate
     * @return
     */
    public static String date(int unixDate) {

        return DateTimeFormatter.ofPattern("MMMM d").format(zonedDateTime(unixDate));
    }

    /**
     *
     * @return
     */
    public static String hourByUtc(int unixDate) {

        return DateTimeFormatter.ofPattern("HH").format(Instant.ofEpochMilli(unixDate * 1000L).atOffset(ZoneOffset.UTC));
    }

    /**
     *
     * @param unixDate
     * @param pattern
     * @return
     */
    public static String time(int unixDate, String pattern) {

        return DateTimeFormatter.ofPattern(pattern).format(zonedDateTime(unixDate));
    }

    /**
     *
     * @return
     */
    public static int currentTimestamp() {

        return (int) Instant.now().getEpochSecond();
    }

    /**
     *
     * @param temp
     * @return
     */
    public static String roundTheTemp(double temp) {

        // No need for long (Math.round returns long) temperature will never be that high number
        return Integer.toString((int) Math.round(temp));
    }

    /**
     *
     * @param temp
     * @return
     */
    public static String roundTheTempDecimal(double temp) {

        return Double.toString((double) Math.round(temp * 10) / 10);
    }

    /**
     *
     * @param city
     * @param context
     */
    public static void saveTheCity(String city, Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.location_key), city);
        editor.apply();
    }

    /**
     *
     * @param context
     * @return
     */
    static String retrieveSavedCity(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        return sharedPref.getString(context.getString(R.string.location_key), null);
    }

    /**
     *
     * @param context
     * @param location
     */
    public static void saveLatAndLon(Context context, Location location) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.latitude_key), Double.toString(location.getLatitude()));
        editor.putString(context.getString(R.string.longitude_key), Double.toString(location.getLongitude()));
        editor.remove(context.getString(R.string.location_key));
        editor.apply();
    }

    static String[] retrieveSavedCoords(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        String lat = sharedPref.getString(context.getString(R.string.latitude_key), null);
        String lon = sharedPref.getString(context.getString(R.string.longitude_key), null);

        return new String[] {lat, lon};
    }
}
