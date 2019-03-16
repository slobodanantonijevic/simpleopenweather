package com.slobodanantonijevic.simpleopenweather.daily;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class CurrentViewModel extends ViewModel {

    LiveData<CurrentWeather> currentForecast;
    private CurrentWeatherRepository currentRepo;
}
