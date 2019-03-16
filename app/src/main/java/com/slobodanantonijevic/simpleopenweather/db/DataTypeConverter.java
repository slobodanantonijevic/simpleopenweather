package com.slobodanantonijevic.simpleopenweather.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slobodanantonijevic.simpleopenweather.hourly.HourForecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import androidx.room.TypeConverter;

public class DataTypeConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<HourForecast> stringToHourlyList (String data) {

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<HourForecast>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String hourlyListToString(List<HourForecast> list) {

        return gson.toJson(list);
    }
}
