package com.slobodanantonijevic.simpleopenweather.general;

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import androidx.fragment.app.Fragment;
import io.reactivex.disposables.CompositeDisposable;

public class Repository {

    protected static final int CURRENT_WEATHER = 0;
    protected static final int DAILY_WEATHER = 1;
    protected static final int HOURLY_WEATHER = 2;

    private static final int ABSOLUTE_DATA_EXPIRY = 15;// * 60; // 45 minutes, no need to update weather more often

    public OpenWeatherApi api;
    //public Context context;

    protected CompositeDisposable disposable;

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

//    public void init() {
//
//        disposable = new CompositeDisposable();
//    }

    /**
     *
     * @param throwable
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

        Log.wtf("REPO", "UPDATE FORECAST WEATHER");
        updateForecastCallback.updateForecastWeather();
    }

    /**
     * Check if current data we've got is outdated per our criteria
     * @return
     */
    public boolean isExpired(int lastDownloadTime) {

        return lastDownloadTime + ABSOLUTE_DATA_EXPIRY < HelpStuff.currentTimestamp();

    }

    /**
     *
     */
//    void dispose() {
//
//        if (disposable != null && !disposable.isDisposed()) {
//
//            //disposable.dispose();
//            disposable.clear();
//        }
//    }

    /**
     * Prepare the callbacks
     */
    protected void interfaceBuilder(Fragment context, Boolean shouldUseForecastCallback) {

        Log.wtf("REPO", "BUILD INTERFACE");

        if (locationCallback == null) {

            locationCallback = (LocationErrorInterface) context;
        }
    }
}
