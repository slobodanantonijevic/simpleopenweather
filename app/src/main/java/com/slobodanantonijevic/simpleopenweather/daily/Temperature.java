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

public class Temperature {

    @SerializedName("day")
    @Expose
    private double dayTemp;

    @SerializedName("min")
    @Expose
    private double minTemp;

    @SerializedName("max")
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
