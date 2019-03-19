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

package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * This is where we describe the database
 */
@Database(entities = {HourlyForecast.class, CurrentWeather.class, Forecast.class}, version = 2)
public abstract class WeatherDb extends RoomDatabase {

    public static final String DB_NAME = "weather.db";

    abstract public HourlyDao hourlyDao();
    abstract public CurrentDao currentDao();
    abstract public DailyDao dailyDao();
}
