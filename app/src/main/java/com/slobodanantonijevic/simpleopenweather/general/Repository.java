package com.slobodanantonijevic.simpleopenweather.general;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import io.reactivex.disposables.CompositeDisposable;

public class Repository {

    public static final int CURRENT_WEATHER = 0;
    public static final int DAILY_WEATHER = 1;
    protected static final int HOURLY_WEATHER = 2;

    protected static final int ABSOLUTE_DATA_EXPIRY = 45 * 60; // 45 minutes, no need to update weather more often

    protected CompositeDisposable disposable = new CompositeDisposable();

    public OpenWeatherApi api;

    public String location;

    protected LocationErrorInterface locationCallback;
    protected UpdateWeatherInterface updateCallback;

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
    public void dispose() {

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
