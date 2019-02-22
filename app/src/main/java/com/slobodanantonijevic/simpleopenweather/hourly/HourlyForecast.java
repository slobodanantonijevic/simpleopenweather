package com.slobodanantonijevic.simpleopenweather.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slobodanantonijevic.simpleopenweather.general.City;

import java.util.List;

public class HourlyForecast {

    @SerializedName("message")
    @Expose
    private double message;

    @SerializedName("list")
    @Expose
    private List<HourForecast> hoursForecast = null;

    @SerializedName("city")
    @Expose
    private City city;

    public double getMessage() {

        return message;
    }

    public void setMessage(double message) {

        this.message = message;
    }

    public List<HourForecast> getHoursForecast() {

        return hoursForecast;
    }

    public void setHoursForecast(List<HourForecast> hoursForecast) {

        this.hoursForecast = hoursForecast;
    }

    public City getCity() {

        return city;
    }

    public void setCity(City city) {

        this.city = city;
    }
}
