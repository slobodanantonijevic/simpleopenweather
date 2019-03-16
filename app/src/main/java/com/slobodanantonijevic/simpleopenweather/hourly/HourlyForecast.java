package com.slobodanantonijevic.simpleopenweather.hourly;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slobodanantonijevic.simpleopenweather.db.DataTypeConverter;
import com.slobodanantonijevic.simpleopenweather.general.City;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class HourlyForecast {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @SerializedName("message")
    @Expose
    private double message;

    @TypeConverters(DataTypeConverter.class)
    @SerializedName("list")
    @Expose
    private List<HourForecast> hoursForecast = null;

    @Embedded(prefix = "city_")
    @SerializedName("city")
    @Expose
    private City city;

    private int lastUpdate = 0;

    /**
     * We'll be using city id here as a primary key
     * @return
     */
    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

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

    public int getLastUpdate() {

        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {

        this.lastUpdate = lastUpdate;
    }
}
