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

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.CurrentDao;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Repository;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CurrentWeatherRepository extends Repository {

    private MutableLiveData<CurrentWeather> data = new MutableLiveData<>();

    private CurrentDao currentDao;

    private CompositeDisposable disposable;

    @Inject
    public CurrentWeatherRepository(OpenWeatherApi api, CurrentDao currentDao) {

        this.api = api;
        this.currentDao = currentDao;
    }

    /**
     * Initializer for mandatory instances
     * @param fragment Fragment instance to bind the interface callbacks to
     */
    void init(Fragment fragment) {

        disposable = new CompositeDisposable();
        interfaceBuilder(fragment);
    }

    /**
     *
     * @param locationId cityId as per OpenWeatherMap API
     * @param location city name
     * @param lat city latitude
     * @param lon city longitude
     * @return LiveData with CurrentWeather model
     */
    LiveData<CurrentWeather> getCurrentWeather(Integer locationId, String location,
                                               String lat, String lon) {

        if (locationId != null) {

            if (data != null && !isExpired(lastUpdate())) {

                return data;
            }

            fetchFromDb(locationId);
            return data;
        }

        refreshData(locationId, location, lat, lon);

        return data;
    }

    /**
     * Fetch the data from the db, if we have any
     * until we can present some fresh data to the user
     * @param locationId cityId as per OpenWeatherMap API
     */
    private void fetchFromDb(Integer locationId) {

        Disposable currentDisposable = currentDao.findById(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {

                            // If we have any data display it to the user before we fetch some fresh details
                            data.setValue(currentWeather);
                            updateWeather();
                        },
                        throwable -> handleRxError(throwable, CURRENT_WEATHER));

        disposable.add(currentDisposable);

        refreshData(locationId, null, null, null);
    }

    /**
     * Insert fresh data into a db
     */
    private void insertIntoDb() {

        Completable.fromAction(() -> currentDao.insert(data.getValue()))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * Get the fresh data from OpenWeather API
     *
     * Any of the below params can be null, as long as at least one version is provided
     * either locationId, location name, or lat & lon, retrofit is smart enough to ignore
     * null params for requests
     * @param locationId cityId as per OpenWeatherMap API
     * @param location city name
     * @param lat city latitude
     * @param lon city longitude
     */
    private void refreshData(Integer locationId, String location, String lat, String lon) {


        Observable<CurrentWeather> call = api.getCurrentWeather(locationId, location, lat, lon);

        Disposable current = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {

                            currentWeather.setLastUpdate(HelpStuff.currentTimestamp());
                            String date = HelpStuff
                                    .time(currentWeather.getUnixDate(), "EEE, LLL dd");
                            currentWeather.setDate(date);

                            data.setValue(currentWeather);

                            // We want to clear the tasks after this as there is no point in holding them anymore
                            disposable.clear();

                            updateWeather();
                            insertIntoDb();
                        },
                        throwable -> handleRxError(throwable, CURRENT_WEATHER));

        disposable.add(current);
    }

    /**
     * Check when was the last time current data is fetched
     * @return The unix timestamp (epoch seconds) of the last update time or 0 if it is unfetchable
     */
    private int lastUpdate() {

        try {

            return data.getValue().getLastUpdate();
        } catch (NullPointerException npe) {

            return 0;
        }
    }

    @Override
    protected void interfaceBuilder(Fragment fragment) {
        super.interfaceBuilder(fragment);

        updateCallback = (UpdateWeatherInterface) fragment;
    }

    /**
     * Called on Fragment's onDestroy, the disposables have to be disposed when the fragment is gone
     */
    void dispose() {

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
