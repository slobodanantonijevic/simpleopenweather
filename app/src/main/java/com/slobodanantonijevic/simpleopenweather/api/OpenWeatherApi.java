package com.slobodanantonijevic.simpleopenweather.api;


import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {

    public static final String BASE_URL = "https://api.openweathermap.org/";

    static final String APP_ID = "60013a62362eda7bbbd86f0e0c56a79a";
    static final String CITY_ID = "id";
    static final String QUERY = "q";
    static final String LAT = "lat";
    static final String LON = "lon";
    static final String PATH = "/data/2.5/";

    @GET(PATH + "weather?units=metric&APPID=" + APP_ID)
    Observable<CurrentWeather> getCurrentWeather (

            @Query(CITY_ID) Integer id,
            @Query(QUERY) String query,
            @Query(LAT) String lat,
            @Query(LON) String lon
    );

    @GET(PATH + "forecast/daily?units=metric&APPID=" + APP_ID)
    Observable<Forecast> getForecast (

            @Query(CITY_ID) Integer id,
            @Query(QUERY) String query,
            @Query(LAT) String lat,
            @Query(LON) String lon
    );

    @GET(PATH + "forecast?units=metric&APPID=" + APP_ID)
    Observable<HourlyForecast> getHourlyForecast (

            @Query(CITY_ID) Integer id,
            @Query(QUERY) String query,
            @Query(LAT) String lat,
            @Query(LON) String lon
    );
}
