package com.slobodanantonijevic.simpleopenweather.daily;

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.general.Repository;
import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class CurrentViewModel extends WeatherViewModel {

    private LiveData<CurrentWeather> currentWeather;
    private CurrentWeatherRepository currentRepo;

    @Inject
    CurrentViewModel(CurrentWeatherRepository currentRepo) {

        this.currentRepo = currentRepo;
    }

    public void init(Fragment fragment, Integer locationId, String location, String lat, String lon) {
        super.init(fragment);

        currentRepo.init(fragment);

        if (currentWeather != null && !currentRepo.isExpired(currentWeather.getValue().getLastUpdate())
                && locationId != null && locationId == currentWeather.getValue().getId()) {

            // Data is here and still valid
            Log.wtf("CURRENT VM", "DATA HERE");
            return;
        }

        currentWeather = currentRepo.getCurrentWeather(locationId, location, lat, lon);
    }

    LiveData<CurrentWeather> getCurrentWeather() {

        return currentWeather;
    }

    @Override
    public Repository getRepo() {

        return currentRepo;
    }

    public void disposeDisposables() {

        currentRepo.dispose();
    }
}
//789128