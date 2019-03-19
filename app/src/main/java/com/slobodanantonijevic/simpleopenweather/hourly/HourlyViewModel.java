package com.slobodanantonijevic.simpleopenweather.hourly;

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.general.Repository;
import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class HourlyViewModel extends WeatherViewModel {

    private LiveData<HourlyForecast> hourlyForecast;
    private HourlyRepository weatherRepo;

    @Inject
    HourlyViewModel(HourlyRepository weatherRepo) {

        this.weatherRepo = weatherRepo;
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

        //weatherRepo.init();
        if (hourlyForecast != null && !weatherRepo.isExpired(weatherRepo.lastUpdate())) {

            // Data is here and still valid
            Log.wtf("HOURLY VM", "DATA HERE");
            return;
        }

        Log.wtf("HOURLY VM LOCATION", location);

        hourlyForecast = weatherRepo.getHourlyForecast(fragment, locationId, location, lat, lon);
    }

    /**
     *
     * @return
     */
    LiveData<HourlyForecast> getHourlyWeather() {

        return hourlyForecast;
    }

    @Override
    public Repository getRepo() {

        return weatherRepo;
    }

    public void disposeDisposables() {

        weatherRepo.dispose();
//        try {
//
//            viewModel.getRepo().dispose();
//        } catch (NullPointerException npe) {
//
//            // We don't really have anything else to do here
//            Log.wtf(viewModel.getClass().getName(), "REPO NULL");
//            npe.printStackTrace();
//
    }
}
