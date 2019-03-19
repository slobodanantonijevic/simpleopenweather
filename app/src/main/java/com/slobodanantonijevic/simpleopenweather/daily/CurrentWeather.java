/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanantonijevic.simpleopenweather.daily;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.slobodanantonijevic.simpleopenweather.db.DataTypeConverter;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.MainWeatherData;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.simpleopenweather.general.Wind;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

// POJO -> Plain Old Java Object
@Entity
public class CurrentWeather {

    @TypeConverters(DataTypeConverter.class)
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    @Embedded(prefix = "main_")
    @SerializedName("main")
    @Expose
    private MainWeatherData main;

    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    @Expose
    private Wind wind;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String cityName;

    @SerializedName("dt")
    @Expose
    private int unixDate; // Sadly this one is not completely dynamic and cannot be used as time update marker

    private String date; // Format L dd
    private String weekDay;
    private int lastUpdate = 0;

    public List<Weather> getWeather() {

        return weather;
    }

    public void setWeather(List<Weather> weather) {

        this.weather = weather;
    }

    public MainWeatherData getMain() {

        return main;
    }

    public void setMain(MainWeatherData main) {

        this.main = main;
    }

    public Wind getWind() {

        return wind;
    }

    public void setWind(Wind wind) {

        this.wind = wind;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getCityName() {

        return cityName;
    }

    public void setCityName(String cityName) {

        this.cityName = cityName;
    }

    public int getUnixDate() {

        return unixDate;
    }

    public void setUnixDate(int unixDate) {

        HelpStuff.date(unixDate);
        this.unixDate = unixDate;
    }

    public String getWeekDay() {

        return weekDay;
    }

    public void setWeekDay(String weekDay) {

        this.weekDay = weekDay;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public int getLastUpdate() {

        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {

        this.lastUpdate = lastUpdate;
    }
}
