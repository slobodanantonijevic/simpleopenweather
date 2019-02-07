package com.slobodanantonijevic.simpleopenweather.daily;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<DayForecast> forecast;
    private Context context;

    /**
     * The view holder for RecyclerView cell
     */
    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        public CustomTextView day;
        public CustomTextView temp;
        public AppCompatImageView icon;

        public ForecastViewHolder(View v) {
            super(v);

            day = v.findViewById(R.id.day);
            temp = v.findViewById(R.id.temp);
            icon = v.findViewById(R.id.weatherIcon);
        }
    }

    public ForecastAdapter(List<DayForecast> forecast, Context context) {

        this.forecast = forecast;
        this.context = context;
    }

    public void update(List<DayForecast> forecast) {

        this.forecast = forecast;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.template_future_weather, viewGroup, false);

        return new ForecastAdapter.ForecastViewHolder(item);
    }

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
    }

    /**
     * Generally it will always be 6, but just to be safe that it does not change on the API side
     * and caches us unprepared
     * @return
     */
    @Override
    public int getItemCount() {

        Log.wtf("THIS WORKS", (forecast.size() - 1) + "");
        return forecast.size() - 1; // We already have today's weather in main container
    }
}
