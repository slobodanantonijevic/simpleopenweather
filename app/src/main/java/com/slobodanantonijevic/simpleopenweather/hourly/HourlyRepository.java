package com.slobodanantonijevic.simpleopenweather.hourly;
import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.HourlyDao;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class HourlyRepository extends Repository {

    private MutableLiveData<HourlyForecast> data = new MutableLiveData<>();

    private HourlyDao hourlyDao;

    @Inject
    public HourlyRepository(OpenWeatherApi api, HourlyDao hourlyDao) {

        this.api = api;
        this.hourlyDao = hourlyDao;
    }

    /**
     *
     * @param location
     * @param lat
     * @param lon
     * @return
     */
    LiveData<HourlyForecast> getHourlyForecast(Fragment fragment, Integer locationId, String location,
                                               String lat, String lon) {

        this.context = fragment.getContext();
        interfaceBuilder(fragment);

        if (locationId != null) {

            location = null; // ignore location name for API calls we already got id

            if (data != null && !isExpired()) {

                    Log.wtf("HOURLY REPO", "OLD DATA");
                    return data;
            }

            fetchFromDb(locationId);
            return data;
        }

        refreshData(locationId, location, lat, lon);

        return data;
    }

    /**
     *
     * @param locationId
     * @return
     */
    private void fetchFromDb(Integer locationId) {

        Log.wtf("HOURLY REPO", "TRYING THE DB DATA");
        Disposable hourlyDb = hourlyDao.findById(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hourlyForecast -> {

                    data.setValue(hourlyForecast);
                    if (hourlyForecast == null || isExpired()) {

                        Log.wtf("HOURLY REPO", "DB DATA OUTDATED");
                        refreshData(locationId, null, null, null);
                    } else {

                        updateWeather();
                        Log.wtf("HOURLY REPO", "DB DATA");
                    }
                }, throwable -> {

                    Log.wtf("HOURLY REPO FETCH", throwable.getCause());
                });

        disposable.add(hourlyDb);
    }

    /**
     *
     */
    private void insertIntoDb() {

        Log.wtf("HOURLY REPO", "INSERTING THE DB DATA");
        Completable.fromAction(() -> hourlyDao.insert(data.getValue()))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     *
     */
    private void refreshData(Integer locationId, String location, String lat, String lon) {

        Observable<HourlyForecast> call = api.getHourlyForecast(locationId, location, lat, lon);

        Log.wtf("HOURLY REPO", "FRESH DATA");
        Disposable hourly = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hourlyForecast -> {

                            hourlyForecast.setLastUpdate(HelpStuff.currentTimestamp());
                            hourlyForecast.setId(hourlyForecast.getCity().getId());

                            data.setValue(hourlyForecast);
                            //
                            updateWeather();
                            insertIntoDb();
                        },
                        throwable -> handleRxError(throwable, HOURLY_WEATHER)); // onError

        disposable.add(hourly);
    }

    /**
     * Prepare the callbacks
     */
    private void interfaceBuilder(Fragment context) {

        if (locationCallback == null) {

            locationCallback = (LocationErrorInterface) context;
        }

        if (updateCallback == null) {

            updateCallback = (UpdateWeatherInterface) context;
        }
    }

    /**
     * Check if current data we've got is outdated per our criteria
     * @return
     */
    boolean isExpired() {

        int lastDownloadTime = 0;
        try {

            lastDownloadTime = data.getValue().getLastUpdate();
        } catch (NullPointerException npe) {

            return true;
        }
        return lastDownloadTime + ABSOLUTE_DATA_EXPIRY < HelpStuff.currentTimestamp();

    }
}
