package com.slobodanantonijevic.simpleopenweather.api;


import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {

    String APP_ID = "60013a62362eda7bbbd86f0e0c56a79a";
    String QUERY = "q";
    String PATH = "/data/2.5/";

    @GET(PATH + "weather?units=metric&APPID=" + APP_ID)
    Observable<CurrentWeather> getCurrentWeather (

            @Query(QUERY) String query
    );

    @GET(PATH + "forecast/daily?units=metric&APPID=" + APP_ID)
    Observable<Forecast> getForecast (

            @Query(QUERY) String query
    );

    @GET(PATH + "forecast?units=metric&APPID=" + APP_ID)
    Observable<HourlyForecast> getHourlyForecast (

            @Query(QUERY) String query
    );
}
