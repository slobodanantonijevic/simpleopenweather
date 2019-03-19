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

package com.slobodanantonijevic.simpleopenweather.general;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import androidx.fragment.app.Fragment;

/**
 * Parent for our repos
 */
public class Repository {

    protected static final int CURRENT_WEATHER = 0;
    protected static final int DAILY_WEATHER = 1;
    protected static final int HOURLY_WEATHER = 2;

    private static final int ABSOLUTE_DATA_EXPIRY = 45 * 60; // 45 minutes, no need to update weather more often

    public OpenWeatherApi api;

    private LocationErrorInterface locationCallback;
    protected UpdateWeatherInterface updateCallback;
    protected UpdateForecastInterface updateForecastCallback;

    /**
     * Interface to callback UI (Fragment) to handle weather fetch errors
     */
    public interface LocationErrorInterface {

        void locationError(Throwable throwable, int occurrence);
    }

    /**
     * Interface to notify UI (Fragment) that fresh data has been downloaded
     */
    public interface UpdateWeatherInterface {

        void updateWeather();
    }

    /**
     * Interface to notify UI (Fragment) that fresh data has been downloaded
     */
    public interface UpdateForecastInterface {

        void updateForecastWeather();
    }

    /**
     * Handle errors
     * @param throwable throwable from rx
     */
    protected void handleRxError(Throwable throwable, int occurrence) {

        locationCallback.locationError(throwable, occurrence);
    }

    /**
     *
     */
    protected void updateWeather() {

        updateCallback.updateWeather();
    }

    /**
     *
     */
    protected void updateForecastWeather() {

        updateForecastCallback.updateForecastWeather();
    }

    /**
     * Check if current data we've got is outdated per our criteria
     * @return if the data has expired
     */
    public boolean isExpired(int lastDownloadTime) {

        return lastDownloadTime + ABSOLUTE_DATA_EXPIRY < HelpStuff.currentTimestamp();

    }

    /**
     * Prepare the callbacks
     */
    protected void interfaceBuilder(Fragment context) {

        if (locationCallback == null) {

            locationCallback = (LocationErrorInterface) context;
        }
    }
}
