package com.slobodanantonijevic.simpleopenweather.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainWeatherData {

    @SerializedName("temp")
    @Expose
    private double currentTemp;

    @SerializedName("pressure")
    @Expose
    private double pressure;

    @SerializedName("humidity")
    @Expose
    private double humidity;

    @SerializedName("temp_min")
    @Expose
    private double minTemp;

    @SerializedName("temp_max")
    @Expose
    private double maxTemp;

    public double getTemp() {

        return currentTemp;
    }

    public void setTemp(double currentTemp) {

        this.currentTemp = currentTemp;
    }

    public double getPressure() {

        return pressure;
    }

    public void setPressure(double pressure) {

        this.pressure = pressure;
    }

    public double getHumidity() {

        return humidity;
    }

    public void setHumidity(double humidity) {

        this.humidity = humidity;
    }

    public double getTempMin() {

        return minTemp;
    }

    public void setTempMin(double minTemp) {

        this.minTemp = minTemp;
    }

    public double getTempMax() {

        return maxTemp;
    }

    public void setTempMax(double maxTemp) {

        this.maxTemp = maxTemp;
    }
}
