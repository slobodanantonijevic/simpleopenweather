package com.slobodanantonijevic.simpleopenweather.daily;

import android.content.Context;
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

    private Context context;

    @Inject
    CurrentWeatherRepository(OpenWeatherApi api, CurrentDao currentDao) {

        this.api = api;
        this.currentDao = currentDao;
    }

    /**
     *
     * @param fragment
     */
    void init(Fragment fragment) {

        this.context = fragment.getContext();
        disposable = new CompositeDisposable();
        interfaceBuilder(fragment);
    }

    /**
     *
     * @param locationId
     * @param location
     * @param lat
     * @param lon
     * @return
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
     *
     * @param locationId
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
     *
     */
    private void insertIntoDb() {

        Completable.fromAction(() -> currentDao.insert(data.getValue()))
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


        Observable<CurrentWeather> call = api.getCurrentWeather(locationId, location, lat, lon);

        Log.wtf("CURRENT REPO", "FRESH DATA");

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
     *
     * @return
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
     *
     */
    void dispose() {

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
