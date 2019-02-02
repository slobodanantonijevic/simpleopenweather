package com.slobodanantonijevic.simpleopenweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temperature {

    @SerializedName("dayTemp")
    @Expose
    private double dayTemp;

    @SerializedName("minTemp")
    @Expose
    private double minTemp;

    @SerializedName("maxTemp")
    @Expose
    private double maxTemp;

    public double getDayTemp() {

        return dayTemp;
    }

    public void setDayTemp(double dayTemp) {

        this.dayTemp = dayTemp;
    }

    public double getMinTemp() {

        return minTemp;
    }

    public void setMinTemp(double minTemp) {

        this.minTemp = minTemp;
    }

    public double getMaxTemp() {

        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {

        this.maxTemp = maxTemp;
    }
}
