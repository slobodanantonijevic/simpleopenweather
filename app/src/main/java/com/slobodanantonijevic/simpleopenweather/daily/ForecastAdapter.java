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

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.ForecastViewHolder;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastViewHolder> {

    private List<DayForecast> forecast;
    private Context context;

    ForecastAdapter(List<DayForecast> forecast, Context context) {

        this.forecast = forecast;
        this.context = context;
    }

    void update(List<DayForecast> forecast) {

        this.forecast = forecast;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.template_forecast_weather, viewGroup, false);

        return new ForecastViewHolder(item, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder viewHolder, int i) {

        // We never want the first one since we have today's weather already
        DayForecast dayForecast = forecast.get(i + 1);

        String day = HelpStuff.weekDay(dayForecast.getDate());
        viewHolder.day.setText(day);

        // We don't really need decimal precision on a weather app
        String temp = HelpStuff.roundTheTemp(dayForecast.getTemp().getDayTemp());
        viewHolder.temp.setText(temp.concat(Weather.TEMPERATURE));

        int weatherId = dayForecast.getWeather().get(0).getId();
        int icon = Weather.findWeatherIcon(weatherId);
        if (icon != 0) {

            viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, icon));
        }

        temp = HelpStuff.roundTheTemp(dayForecast.getTemp().getMinTemp());
        viewHolder.minTemp.setText(temp.concat(Weather.TEMPERATURE));

        temp = HelpStuff.roundTheTemp(dayForecast.getTemp().getMaxTemp());
        viewHolder.maxTemp.setText(temp.concat(Weather.TEMPERATURE));

        viewHolder.pressure.setText(Double.toString(dayForecast.getPressure()).concat(Weather.PRESSURE));
        viewHolder.humidity.setText(Double.toString(dayForecast.getHumidity()).concat(Weather.HUMIDITY));
        viewHolder.wind.setText(Double.toString(dayForecast.getSpeed()).concat(Weather.WIND));
    }

    /**
     * Generally it will always be 6, but just to be safe that it does not change on the API side
     * and caches us unprepared
     * @return size of the items list
     */
    @Override
    public int getItemCount() {

        return forecast.size() - 1; // We already have today's weather in main container
    }
}
