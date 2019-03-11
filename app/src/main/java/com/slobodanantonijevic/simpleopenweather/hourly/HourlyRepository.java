package com.slobodanantonijevic.simpleopenweather.hourly;
import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class HourlyRepository extends Repository {

    private MutableLiveData<HourlyForecast> data = new MutableLiveData<>();

    private int lastDownloadTime = 0;

    @Inject
    public HourlyRepository(OpenWeatherApi api) {

        this.api = api;
    }

    /**
     *
     * @param location
     * @param lat
     * @param lon
     * @return
     */
    LiveData<HourlyForecast> getHourlyForecast(Fragment ctx, String location, String lat, String lon) {

        interfaceBuilder(ctx);

        this.location = location;

        if (data != null && !isExpired()) {

            Log.wtf("HOURLY", "OLD DATA");
            return data;
        }

        Observable<HourlyForecast> call = api.getHourlyForecast(location, lat, lon);

        Log.wtf("HOURLY", "FRESH DATA");
        Disposable hourly = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hourlyForecast -> {

                    lastDownloadTime = HelpStuff.currentTimestamp();
                    data.setValue(hourlyForecast);
                    updateWeather();
                    },
                        throwable -> handleRxError(throwable, HOURLY_WEATHER)); // onError

        disposable.add(hourly);

        return data;
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
    private boolean isExpired() {

        return lastDownloadTime + ABSOLUTE_DATA_EXPIRY < HelpStuff.currentTimestamp();

    }
}
