package com.slobodanantonijevic.simpleopenweather.general;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {

    public OpenWeatherApi api;

    public Fragment context;

    public String location;
    public String lat;
    public String lon;

    /**
     *
     * @param ctx
     * @param location
     * @param lat
     * @param lon
     */
    public void init(Fragment ctx, String location, String lat, String lon) {

        api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        this.context = ctx;

        this.location = location;
        this.lat = lat;
        this.lon = lon;
    }
}
