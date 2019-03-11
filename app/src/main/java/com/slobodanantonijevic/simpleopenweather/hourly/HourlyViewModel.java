package com.slobodanantonijevic.simpleopenweather.hourly;

import android.util.Log;

import com.slobodanantonijevic.simpleopenweather.general.WeatherViewModel;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

public class HourlyViewModel extends WeatherViewModel {

    private LiveData<HourlyForecast> hourlyForecast;
    private HourlyRepository weatherRepo;

    @Inject
    HourlyViewModel(HourlyRepository weatherRepo) {

        this.weatherRepo = weatherRepo;
    }

    @Override
    public void init(Fragment ctx, String location, String lat, String lon) {
        super.init(ctx, location, lat, lon);

        if (this.hourlyForecast != null) {

            Log.wtf("HOURLY", "DATA HERE");
            return;
        }

        Log.wtf("HOURLY VM LOCATION", location);

        hourlyForecast = weatherRepo.getHourlyForecast(ctx, location, lat, lon);
    }

    /**
     *
     * @return
     */
    LiveData<HourlyForecast> getHourlyWeather() {

        return hourlyForecast;
    }

    /**
     *
     */
    void disposeDisposables() {

        weatherRepo.dispose();
    }
}
