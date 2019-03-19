package com.slobodanantonijevic.simpleopenweather.general;

import android.util.Log;

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

    /**
     *
     */
//    public void disposeDisposables(WeatherViewModel viewModel) {
//
//        viewModel.getRepo().dispose();
////        try {
////
////            viewModel.getRepo().dispose();
////        } catch (NullPointerException npe) {
////
////            // We don't really have anything else to do here
////            Log.wtf(viewModel.getClass().getName(), "REPO NULL");
////            npe.printStackTrace();
////        }
//    }

    public Repository getRepo() {

        return null;
    }
}
