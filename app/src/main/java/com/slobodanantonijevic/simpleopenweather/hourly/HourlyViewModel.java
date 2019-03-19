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

package com.slobodanantonijevic.simpleopenweather.hourly;

import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class HourlyViewModel extends WeatherViewModel {

    private LiveData<HourlyForecast> hourlyForecast;
    private HourlyRepository weatherRepo;

    @Inject
    HourlyViewModel(HourlyRepository weatherRepo) {

        this.weatherRepo = weatherRepo;
    }

    /**
     *
     * @param fragment Fragment instance to bind the interface callbacks and context to
     * @param locationId cityId as per OpenWeatherMap API
     * @param location city name
     * @param lat city latitude
     * @param lon city longitude
     */
    public void init(Fragment fragment, Integer locationId, String location, String lat, String lon) {
        super.init(fragment);

        weatherRepo.init(fragment);

        if (hourlyForecast != null && !weatherRepo.isExpired(weatherRepo.lastUpdate())
                && locationId != null && locationId == hourlyForecast.getValue().getId()) {

            // Data is here and still valid
            return;
        }

        hourlyForecast = weatherRepo.getHourlyForecast(locationId, location, lat, lon);
    }

    LiveData<HourlyForecast> getHourlyWeather() {

        return hourlyForecast;
    }

    /**
     *  Dispose the disposables held by the repo
     */
    void disposeDisposables() {

        weatherRepo.dispose();
    }
}
