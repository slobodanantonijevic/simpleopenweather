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

package com.slobodanantonijevic.simpleopenweather.api;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApi {

    String BASE_URL = "https://api.openweathermap.org/";

    String APP_ID = "60013a62362eda7bbbd86f0e0c56a79a";
    String CITY_ID = "id";
    String QUERY = "q";
    String LAT = "lat";
    String LON = "lon";
    String PATH = "/data/2.5/";

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
