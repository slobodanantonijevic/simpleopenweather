package com.slobodanantonijevic.simpleopenweather.daily;

import android.content.Context;
import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.DailyDao;
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

public class ForecastRepository extends Repository {

    MutableLiveData<Forecast> data = new MutableLiveData<>();

    private DailyDao dailyDao;

    private CompositeDisposable disposable;

    @Inject
    ForecastRepository(OpenWeatherApi api, DailyDao dailyDao) {

        this.api = api;
        this.dailyDao = dailyDao;
    }

    /**
     *
     * @param fragment
     */
    void init(Fragment fragment) {

        disposable = new CompositeDisposable();
        interfaceBuilder(fragment, false);
    }

    /**
     *
     * @param fragment
     * @param locationId
     * @param location
     * @param lat
     * @param lon
     * @return
     */
    LiveData<Forecast> getDailyForecast(Fragment fragment, Integer locationId, String location,
                                        String lat, String lon) {

        Log.wtf("FORECAST", "BUILD INTERFACE");
        // We do not want a location error on this one as it will already be handled on current weather
        interfaceBuilder(fragment, true);

        if (locationId != null) {

            if (data != null && !isExpired(lastUpdate())) {

                Log.wtf("DAILY REPO", "OLD DATA");
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
     */
    private void fetchFromDb(Integer locationId) {

        Log.wtf("DAILY REPO", "TRYING THE DB DATA");
        Disposable currentDisposable = dailyDao.findById(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecast -> {

                            data.setValue(forecast);

                            updateForecastWeather();
                        },
                        throwable -> Log.wtf("DAILY REPO FETCH", throwable.getCause()));

        disposable.add(currentDisposable);

        refreshData(locationId, null, null, null);
    }

    /**
     *
     */
    private void insertIntoDb() {

        Log.wtf("DAILY REPO", "INSERTING THE DB DATA");
        Completable.fromAction(() -> dailyDao.insert(data.getValue()))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     *
     * @param locationId
     * @param location
     * @param lat
     * @param lon
     */
    private void refreshData(Integer locationId, String location, String lat, String lon) {

        Observable<Forecast> call = api.getForecast(locationId, location, lat, lon);

        Log.wtf("DAILY REPO", "FRESH DATA");

        Disposable current = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecast -> {

                            forecast.setLastUpdate(HelpStuff.currentTimestamp());
                            forecast.setId(forecast.getCity().getId());
                            data.setValue(forecast);
                            // We want to clear the tasks after this as there is no point in holding them anymore
                            disposable.clear();
                            updateForecastWeather();
                            insertIntoDb();
                        },
                        throwable -> handleRxError(throwable, DAILY_WEATHER));

        disposable.add(current);
    }

    /**
     *
     * @return
     */
    int lastUpdate() {

        try {

            return data.getValue().getLastUpdate();
        } catch (NullPointerException npe) {

            return 0;
        }
    }

    @Override
    protected void interfaceBuilder(Fragment context, Boolean shouldUseForecastCallback) {
        super.interfaceBuilder(context, shouldUseForecastCallback);

        updateForecastCallback = (UpdateForecastInterface) context;
    }

    void dispose() {

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
