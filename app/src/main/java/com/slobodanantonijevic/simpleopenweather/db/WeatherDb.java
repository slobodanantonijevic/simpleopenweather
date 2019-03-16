package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HourlyForecast.class}, version = 1)
public abstract class WeatherDb extends RoomDatabase {

    public static final String DB_NAME = "weather.db";

    abstract public HourlyDao hourlyDao();
}
