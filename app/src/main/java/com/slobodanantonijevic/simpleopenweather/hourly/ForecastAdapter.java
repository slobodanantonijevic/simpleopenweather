package com.slobodanantonijevic.simpleopenweather.hourly;

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

    private List<HourForecast> forecast;
    private Context context;

    ForecastAdapter(List<HourForecast> forecast, Context context) {

        this.forecast = forecast;
        this.context = context;
    }

    void update(List<HourForecast> forecast) {

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
        HourForecast hourForecast = forecast.get(i);

        int unixTimeStamp = hourForecast.getDate();
        if (i == 0 || HelpStuff.hourByUtc(unixTimeStamp).equals("00")) {

            viewHolder.date.setVisibility(View.VISIBLE);
            String date = HelpStuff.time(unixTimeStamp, "LLL dd");
            viewHolder.date.setText(date);
        } else if (viewHolder.date.getVisibility() == View.VISIBLE) {

            viewHolder.date.setVisibility(View.GONE);
        }
        String day = HelpStuff.time(unixTimeStamp, "h a");
        viewHolder.day.setText(day);

        // We don't really need decimal precision on a weather app
        String temp = HelpStuff.roundTheTempDecimal(hourForecast.getConditions().getTemp());
        viewHolder.temp.setText(temp.concat(Weather.TEMPERATURE));

        int weatherId = hourForecast.getWeather().get(0).getId();
        int icon = Weather.findWeatherIcon(weatherId);
        if (icon != 0) {

            viewHolder.icon.setImageDrawable(ContextCompat.getDrawable(context, icon));
        }

        temp = HelpStuff.roundTheTempDecimal(hourForecast.getConditions().getTempMin());
        viewHolder.minTemp.setText(temp.concat(Weather.TEMPERATURE));

        temp = HelpStuff.roundTheTempDecimal(hourForecast.getConditions().getTempMax());
        viewHolder.maxTemp.setText(temp.concat(Weather.TEMPERATURE));

        viewHolder.pressure.setText(Double.toString(hourForecast.getConditions().getPressure()).concat(Weather.PRESSURE));
        viewHolder.humidity.setText(Double.toString(hourForecast.getConditions().getHumidity()).concat(Weather.HUMIDITY));
        viewHolder.wind.setText(Double.toString(hourForecast.getWind().getSpeed()).concat(Weather.WIND));
    }

    @Override
    public int getItemCount() {

        return forecast.size();
    }
}
