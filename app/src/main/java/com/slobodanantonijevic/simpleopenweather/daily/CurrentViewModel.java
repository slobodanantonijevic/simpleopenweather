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

package com.slobodanantonijevic.simpleopenweather.daily;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.CurrentDao;
import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CurrentViewModel extends WeatherViewModel {

    private CurrentDao currentWeatherDao;

    @Inject
    CurrentViewModel(OpenWeatherApi api, CurrentDao currentWeatherDao) {

        this.api = api;
        this.currentWeatherDao = currentWeatherDao;
    }

    /**
     * Get the last stored weather data
     * @param cityId to fetch the last data we have stored
     *
     * @return  a [Flowable] that emits every time the data has been updated
     */
    public Flowable<CurrentWeather> currentWeather(Integer cityId) {

        return currentWeatherDao.findById(cityId);
    }

    /**
     *
     * Search for the fresh data from the API, at least one of the params should be provided.
     * It is best practice to provide just one as providing both not null can lead to conflict on API side
     *
     * @param cityId to search for, can be null
     * @param cityName to search for, can be null
     *
     * @return a [Single] containing the fresh weather
     */
    public Single<CurrentWeather> getFreshWeather(Integer cityId, String cityName) {

        return api.getCurrentWeather(cityId, cityName);
    }

    /**
     * Update current weather data in the local db
     * @param weather Fresh current weather
     *
     * @return a [Completable] that completes when the new weather has been stored
     */
    public Completable updateWeatherData(CurrentWeather weather) {

        return currentWeatherDao.insert(weather);
    }
}