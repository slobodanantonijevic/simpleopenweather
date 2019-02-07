package com.slobodanantonijevic.simpleopenweather.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slobodanantonijevic.simpleopenweather.general.MainWeatherData;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.simpleopenweather.general.Wind;

import java.util.List;

public class HourForecast {

    @SerializedName("dt")
    @Expose
    private int date;

    @SerializedName("main")
    @Expose
    private MainWeatherData conditions;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public MainWeatherData getConditions() {
        return conditions;
    }

    public void setConditions(MainWeatherData conditions) {
        this.conditions = conditions;
    }


    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Wind getWind() {

        return wind;
    }

    public void setWind(Wind wind) {

        this.wind = wind;
    }
}
