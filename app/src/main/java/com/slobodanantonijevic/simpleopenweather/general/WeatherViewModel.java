package com.slobodanantonijevic.simpleopenweather.general;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    public OpenWeatherApi api;

    public Fragment fragment;

    /**
     *
     * @param fragment
     */
    public void init(Fragment fragment) {

        api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        this.fragment = fragment;
    }
}
