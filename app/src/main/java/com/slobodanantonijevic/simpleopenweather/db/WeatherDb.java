package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import javax.inject.Inject;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {HourlyForecast.class, CurrentWeather.class, Forecast.class}, version = 2)
public abstract class WeatherDb extends RoomDatabase {

    public static final String DB_NAME = "weather.db";

    abstract public HourlyDao hourlyDao();
    abstract public CurrentDao currentDao();
    abstract public DailyDao dailyDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };
}
