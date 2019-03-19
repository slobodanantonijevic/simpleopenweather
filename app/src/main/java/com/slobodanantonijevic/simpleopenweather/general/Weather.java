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
import com.slobodanantonijevic.simpleopenweather.R;

public class Weather {

    // Some static config stuff mainly the markings and measurements
    public static final String HUMIDITY = " %";
    public static final String PRESSURE = " hPa";
    public static final String WIND = " m/s";
    public static final String TEMPERATURE = "°";

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("main")
    @Expose
    private String conditions;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getConditions() {

        return conditions;
    }

    public void setConditions(String main) {

        this.conditions = main;
    }

    /**
     * Static will suffice
     * @param weatherId
     * @return
     */
    public static int findWeatherIcon(int weatherId) {

        int icon = 0;
        if (weatherId == 800) {

            icon = R.drawable.clear_day;
        } else if (weatherId > 800) {

            icon = R.drawable.cloudy_day;
        } else if (weatherId >= 700) {

            icon = R.drawable.mist;
        } else if (weatherId >= 600) {

            icon = R.drawable.snow;
        } else if (weatherId >= 511) {

            icon = R.drawable.rain_shower;
        } else if (weatherId >= 500) {

            icon = R.drawable.rain;
        } else if (weatherId >= 300) {

            icon = R.drawable.rain_shower;
        } else if (weatherId >= 200) {

            icon = R.drawable.thunderstorm;
        }

        return icon;
    }
}
