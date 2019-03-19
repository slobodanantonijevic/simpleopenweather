/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
     * @param unixDate unix timestamp (seconds)
     * @return weekday
     */
    public static String weekDay(int unixDate) {

        return DateTimeFormatter.ofPattern("EEEE").format(zonedDateTime(unixDate));
    }

    /**
     *
     * @param unixDate unix timestamp (seconds)
     * @return date in MMMM d pattern
     */
    public static String date(int unixDate) {

        return DateTimeFormatter.ofPattern("MMMM d").format(zonedDateTime(unixDate));
    }

    /**
     *
     * @param unixDate unix timestamp (seconds)
     * @return hour in HH pattern
     */
    public static String hourByUtc(int unixDate) {

        return DateTimeFormatter.ofPattern("HH").format(Instant.ofEpochMilli(unixDate * 1000L).atOffset(ZoneOffset.UTC));
    }

    /**
     *
     * @param unixDate unix timestamp (seconds)
     * @param pattern
     * @return time in desired pattern
     */
    public static String time(int unixDate, String pattern) {

        return DateTimeFormatter.ofPattern(pattern).format(zonedDateTime(unixDate));
    }

    /**
     *
     * @return curent unix timestamp
     */
    public static int currentTimestamp() {

        return (int) Instant.now().getEpochSecond();
    }

    /**
     *
     * @param temp temperature as double with possible decimals
     * @return round value for temperature (integer)
     */
    public static String roundTheTemp(double temp) {

        // No need for long (Math.round returns long) temperature will never be that high number
        return Integer.toString((int) Math.round(temp));
    }

    /**
     *
     * @param temp temperature as double with possible 4 decimals
     * @return temperature as double rounded to one decimal
     */
    public static String roundTheTempDecimal(double temp) {

        return Double.toString((double) Math.round(temp * 10) / 10);
    }

    /**
     *
     * @param city city name
     * @param context context to provide to editor
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
     * @param cityId city id
     * @param context context to provide to editor
     */
    public static void saveTheCityId(Integer cityId, Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.location_id_key), cityId);
        editor.apply();
    }

    /**
     *
     * @param context context to provide to editor
     */
    public static void removeTheCityId(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.location_id_key), -1);
        editor.apply();
    }

    /**
     *
     * @param context context to provide to shared pref
     * @return saved current city id
     */
    static int retrieveSavedCityId(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        return sharedPref.getInt(context.getString(R.string.location_id_key), -1);
    }

    /**
     *
     * @param context context to provide to shared pref
     * @return saved current city name
     */
    static String retrieveSavedCity(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        return sharedPref.getString(context.getString(R.string.location_key), null);
    }

    /**
     *
     * @param context context to provide to editor
     * @param location geolocation
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

    /**
     *
     * @param context context to provide to shared pref
     * @return lat & lon stored
     */
    static String[] retrieveSavedCoords(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                BASIC_CONFIG_FILE, Context.MODE_PRIVATE);

        String lat = sharedPref.getString(context.getString(R.string.latitude_key), null);
        String lon = sharedPref.getString(context.getString(R.string.longitude_key), null);

        return new String[] {lat, lon};
    }
}
