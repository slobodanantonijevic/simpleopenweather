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
