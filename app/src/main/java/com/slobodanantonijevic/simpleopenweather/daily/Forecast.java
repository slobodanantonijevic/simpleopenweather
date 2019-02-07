package com.slobodanantonijevic.simpleopenweather.daily;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("cod")
    @Expose
    private String cod;

    @SerializedName("list")
    @Expose
    private List<DayForecast> daysForecast = null;

    public String getCod() {

        return cod;
    }

    public void setCod(String cod) {

        this.cod = cod;
    }

    public List<DayForecast> getDaysForecast() {

        return daysForecast;
    }

    public void setDaysForecast(List<DayForecast> daysForecast) {

        this.daysForecast = daysForecast;
    }
}
