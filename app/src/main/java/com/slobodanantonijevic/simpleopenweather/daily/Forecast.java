package com.slobodanantonijevic.simpleopenweather.daily;

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
public class Forecast {

    @SerializedName("cod")
    @Expose
    private String cod;

    @TypeConverters(DataTypeConverter.class)
    @SerializedName("list")
    @Expose
    private List<DayForecast> daysForecast = null;

    @Embedded(prefix = "city_")
    @SerializedName("city")
    @Expose
    private City city;

    @PrimaryKey
    @SerializedName("id")
    private int id;

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
