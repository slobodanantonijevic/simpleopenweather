package com.slobodanantonijevic.simpleopenweather.api;

import com.slobodanantonijevic.simpleopenweather.model.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.model.Forecast;
import com.slobodanantonijevic.simpleopenweather.model.HourlyForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {

    String APP_ID = "60013a62362eda7bbbd86f0e0c56a79a";

    @GET("/data/2.5/weather?units=imperial&APPID=" + APP_ID)
    Call<CurrentWeather> getCurrentWeather (

            @Query("q") String city
    );

    @GET("/data/2.5/forecast/daily?units=imperial&APPID=" + APP_ID)
    Call<Forecast> getForecast (

            @Query("q") String city
    );

    @GET("/data/2.5/forecast?units=imperial&APPID=" + APP_ID)
    Call<HourlyForecast> getHourlyForecast (

            @Query("q") String city
    );
}
