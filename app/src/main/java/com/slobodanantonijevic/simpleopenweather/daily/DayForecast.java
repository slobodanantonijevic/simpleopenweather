package com.slobodanantonijevic.simpleopenweather.daily;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.Weather;

import java.util.List;
import java.util.Locale;

public class DayForecast {

    @SerializedName("dt")
    @Expose
    private int date;

    @SerializedName("temp")
    @Expose
    private Temperature temp;

    @SerializedName("pressure")
    @Expose
    private double pressure;

    @SerializedName("humidity")
    @Expose
    private double humidity;

    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @SerializedName("speed")
    @Expose
    private double speed;

    public int getDate() {

        return date;
    }

    public void setDate(int date) {

        this.date = date;
    }

    public Temperature getTemp() {

        return temp;
    }

    public void setTemp(Temperature temp) {

        this.temp = temp;
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

    public List<Weather> getWeather() {

        return weather;
    }

    public void setWeather(List<Weather> weather) {

        this.weather = weather;
    }

    public double getSpeed() {

        return speed;
    }

    public void setSpeed(double speed) {

        this.speed = speed;
    }
}

