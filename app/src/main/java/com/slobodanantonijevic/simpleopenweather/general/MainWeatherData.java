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

package com.slobodanantonijevic.simpleopenweather.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainWeatherData {

    @SerializedName("temp")
    @Expose
    private double temp;

    @SerializedName("pressure")
    @Expose
    private double pressure;

    @SerializedName("humidity")
    @Expose
    private double humidity;

    @SerializedName("temp_min")
    @Expose
    private double tempMin;

    @SerializedName("temp_max")
    @Expose
    private double tempMax;

    public double getTemp() {

        return temp;
    }

    public void setTemp(double temp) {

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

    public double getTempMin() {

        return tempMin;
    }

    public void setTempMin(double tempMin) {

        this.tempMin = tempMin;
    }

    public double getTempMax() {

        return tempMax;
    }

    public void setTempMax(double tempMax) {

        this.tempMax = tempMax;
    }
}
