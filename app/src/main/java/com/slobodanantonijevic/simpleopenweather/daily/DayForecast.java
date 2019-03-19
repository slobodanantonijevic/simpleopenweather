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
import com.slobodanantonijevic.simpleopenweather.general.Weather;

import java.util.List;

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

