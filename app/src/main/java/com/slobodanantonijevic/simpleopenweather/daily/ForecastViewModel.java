package com.slobodanantonijevic.simpleopenweather.daily;

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.general.Repository;
import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class ForecastViewModel extends WeatherViewModel {

    private LiveData<Forecast> forecastWeather;
    private ForecastRepository forecastRepo;

    @Inject
    ForecastViewModel(ForecastRepository forecastRepo) {

        this.forecastRepo = forecastRepo;
    }

    /**
     *
     * @param fragment
     * @param locationId
     * @param location
     * @param lat
     * @param lon
     */
    public void init(Fragment fragment, Integer locationId, String location, String lat, String lon) {
        super.init(fragment);

        forecastRepo.init(fragment);
        if (forecastWeather != null && !forecastRepo.isExpired(forecastWeather.getValue().getLastUpdate())
                && locationId != null) {

            // Data is here and still valid
            Log.wtf("FORECAST VM", "DATA HERE");
            return;
        }

        forecastWeather = forecastRepo.getDailyForecast(fragment, locationId, location, lat, lon);
    }

    /**
     *
     * @return
     */
    public LiveData<Forecast> getDailyForecast() {

        return forecastWeather;
    }

    public void disposeDisposables() {

        forecastRepo.dispose();
    }
}
