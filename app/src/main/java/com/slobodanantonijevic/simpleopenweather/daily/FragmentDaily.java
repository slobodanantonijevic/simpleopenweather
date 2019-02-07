package com.slobodanantonijevic.simpleopenweather.daily;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDaily extends Fragment {

    /**
     * Generally we did not have to put them as globals but since we'll add a refresh functionality
     * preventing multiple findViewById(s) (on each refresh), seems like a smart idea
     */
    private CustomTextView dateField;
    private CustomTextView dayField;
    private CustomTextView cityField;
    private CustomTextView temperatureField;
    private CustomTextView pressureField;
    private CustomTextView humidityField;
    private CustomTextView windField;
    private CustomTextView minTempField;
    private CustomTextView maxTempField;
    private AppCompatImageView weatherImage;

    // Forecast fields & values
    private List<DayForecast> daysForecast = new ArrayList<>();
    private ForecastAdapter forecastAdapter;

    public FragmentDaily() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findCurrentWeather();
        findForecast();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily, container, false);

        RecyclerView forecast = rootView.findViewById(R.id.forecastHolder);

        forecastAdapter = new ForecastAdapter(daysForecast, getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        forecast.setLayoutManager(layoutManager);
        forecast.setItemAnimator(new DefaultItemAnimator());
        forecast.setAdapter(forecastAdapter);

        locateCurrentWeatherFields(rootView);

        return rootView;
    }

    /**
     *
     * @param rootView
     */
    private void locateCurrentWeatherFields(View rootView) {

        dateField = rootView.findViewById(R.id.date);
        dayField = rootView.findViewById(R.id.day);
        cityField = rootView.findViewById(R.id.city);
        temperatureField = rootView.findViewById(R.id.currentTemperature);
        pressureField = rootView.findViewById(R.id.pressure);
        humidityField = rootView.findViewById(R.id.humidity);
        windField = rootView.findViewById(R.id.wind);
        minTempField = rootView.findViewById(R.id.minTemp);
        maxTempField = rootView.findViewById(R.id.maxTemp);
        weatherImage = rootView.findViewById(R.id.weatherIcon);
    }

    /**
     *
     */
    private void findCurrentWeather() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Call<CurrentWeather> call = api.getCurrentWeather("London,UK");

        Log.wtf("URL_CALLED", call.request().url() + "");

        call.enqueue(new Callback<CurrentWeather>() {

            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {

                CurrentWeather weather = response.body();

                weather.setWeekDay(HelpStuff.weekDay(weather.getUnixDate()));
                weather.setDate(HelpStuff.date(weather.getUnixDate()));

                displayCurrentWeather(weather);
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

                Toast.makeText(getContext(), "A PROBLEM OCCURED BUDDY!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    private void findForecast() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Call<Forecast> call = api.getForecast("London,UK");

        call.enqueue(new Callback<Forecast>() {

            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {

                Forecast forecast = response.body();
                displayForecast(forecast);
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

                Toast.makeText(getContext(), "A PROBLEM OCCURED BUDDY!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     * @param weather
     */
    private void displayCurrentWeather(CurrentWeather weather) {

        dateField.setText(weather.getDate());
        dayField.setText(weather.getWeekDay());

        cityField.setText(weather.getCityName());

        String temperature = HelpStuff.roundTheTemp(weather.getMain().getTemp());
        temperatureField.setText(temperature.concat(Weather.TEMPERATURE));

        // We could have used String.format() here to get the right display as per Locale
        pressureField.setText(Double.toString(weather.getMain().getPressure()).concat(Weather.PRESSURE));
        humidityField.setText(Double.toString(weather.getMain().getHumidity()).concat(Weather.HUMIDITY));
        windField.setText(Double.toString(weather.getWind().getSpeed()).concat(Weather.WIND));

        // No need to initialize more variables, since we have the appropriate one
        temperature = HelpStuff.roundTheTemp(weather.getMain().getTempMin());
        minTempField.setText(temperature.concat(Weather.TEMPERATURE));
        temperature = HelpStuff.roundTheTemp(weather.getMain().getTempMax());
        maxTempField.setText(temperature.concat(Weather.TEMPERATURE));

        int weatherId = weather.getWeather().get(0).getId();
        int icon = Weather.findWeatherIcon(weatherId);
        if (icon != 0) {

            weatherImage.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
        }
    }

    /**
     *
     * @param forecast
     */
    private void displayForecast(Forecast forecast) {

        daysForecast = forecast.getDaysForecast();
        Log.wtf("URL_CALLED", daysForecast.size() + "");
        forecastAdapter.update(daysForecast);
        //forecastAdapter.notifyDataSetChanged();
    }
}
