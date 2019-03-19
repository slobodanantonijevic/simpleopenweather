package com.slobodanantonijevic.simpleopenweather.hourly;
import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.HourlyDao;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Repository;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class HourlyRepository extends Repository {

    private MutableLiveData<HourlyForecast> data = new MutableLiveData<>();

    private HourlyDao hourlyDao;

    protected CompositeDisposable disposable = new CompositeDisposable();

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

        //this.context = fragment.getContext();
        interfaceBuilder(fragment, false);

        if (locationId != null) {

            if (data != null && !isExpired(lastUpdate())) {

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
        Disposable hourlyDisposable = hourlyDao.findById(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hourlyForecast -> {

                    data.setValue(hourlyForecast);
                    if (hourlyForecast == null || isExpired(lastUpdate())) {

                        Log.wtf("HOURLY REPO", "DB DATA OUTDATED");
                        refreshData(locationId, null, null, null);
                    } else {

                        Log.wtf("HOURLY REPO", "DB DATA");
                        updateWeather();
                    }
                },
                throwable -> handleRxError(throwable, CURRENT_WEATHER));

        disposable.add(hourlyDisposable);
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

                            updateWeather();
                            insertIntoDb();
                        },
                        throwable -> handleRxError(throwable, HOURLY_WEATHER)); // onError

        disposable.add(hourly);
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

        if (updateCallback == null) {

            updateCallback = (UpdateWeatherInterface) context;
        }
    }

    void dispose() {

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
