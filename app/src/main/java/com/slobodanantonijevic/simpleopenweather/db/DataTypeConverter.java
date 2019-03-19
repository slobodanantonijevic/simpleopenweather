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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slobodanantonijevic.simpleopenweather.daily.DayForecast;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.simpleopenweather.hourly.HourForecast;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

/**
 * Type Converter to instruct Room how to serialize and deserialize List(s) of data
 */
public class DataTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    static List<HourForecast> stringToHourlyList(String data) {

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<HourForecast>>() {}.getType();

        return gson.fromJson(data, listType);
    }


    @TypeConverter
    static String hourlyListToString(List<HourForecast> list) {

        return gson.toJson(list);
    }

    @TypeConverter
    static List<Weather> stringToWeatherList (String data) {

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Weather>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    static String weatherListToString(List<Weather> list) {

        return gson.toJson(list);
    }

    @TypeConverter
    static List<DayForecast> stringToDailyList (String data) {

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<DayForecast>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    static String dailyListToString(List<DayForecast> list) {

        return gson.toJson(list);
    }
}
