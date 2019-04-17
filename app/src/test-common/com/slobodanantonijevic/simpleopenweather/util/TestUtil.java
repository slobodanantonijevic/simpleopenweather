package com.slobodanantonijevic.simpleopenweather.util;

import com.google.gson.Gson;
import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TestUtil {

    public static LiveData<CurrentWeather> createCurrentWeather(String json) {

        Gson gson = new Gson();
        return new MutableLiveData<>(
                 gson.fromJson(json, CurrentWeather.class));
    }

    public static LiveData<Forecast> createForecast(String json) {

        Gson gson = new Gson();
        return new MutableLiveData<>(
                gson.fromJson(json, Forecast.class));
    }

    public static LiveData<HourlyForecast> createHourlyForecast(String json) {

        Gson gson = new Gson();
        return new MutableLiveData<>(
                gson.fromJson(json, HourlyForecast.class));
    }
}
