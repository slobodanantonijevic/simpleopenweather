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

import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class CurrentViewModel extends WeatherViewModel {

    private LiveData<CurrentWeather> currentWeather;
    private CurrentWeatherRepository currentRepo;

    @Inject
    CurrentViewModel(CurrentWeatherRepository currentRepo) {

        this.currentRepo = currentRepo;
    }

    /**
     * @param fragment Fragment instance to bind the interface callbacks and context to
     * @param locationId cityId as per OpenWeatherMap API
     * @param location city name
     * @param lat city latitude
     * @param lon city longitude
     */
    public void init(Fragment fragment, Integer locationId, String location, String lat, String lon) {
        super.init(fragment);

        currentRepo.init(fragment);

        if (currentWeather != null && !currentRepo.isExpired(currentWeather.getValue().getLastUpdate())
                && locationId != null && locationId == currentWeather.getValue().getId()) {

            // Data is here and still valid
            return;
        }

        currentWeather = currentRepo.getCurrentWeather(locationId, location, lat, lon);
    }

    LiveData<CurrentWeather> getCurrentWeather() {

        return currentWeather;
    }

    /**
     *  Dispose the disposables held by the repo
     */
    void disposeDisposables() {

        currentRepo.dispose();
    }
}