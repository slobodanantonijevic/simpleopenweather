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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

public class FragmentDaily extends FragmentForecast {

    private ForecastAdapter forecastAdapter;

    // Butter Knife
    @BindView(R.id.date) CustomTextView dateField;
    @BindView(R.id.city) CustomTextView cityField;
    @BindView(R.id.currentTemperature) CustomTextView temperatureField;
    @BindView(R.id.pressure) CustomTextView pressureField;
    @BindView(R.id.humidity) CustomTextView humidityField;
    @BindView(R.id.wind) CustomTextView windField;
    @BindView(R.id.minTemp) CustomTextView minTempField;
    @BindView(R.id.maxTemp) CustomTextView maxTempField;
    @BindView(R.id.weatherIcon) AppCompatImageView weatherImage;

    // ViewModels' data
    private CurrentViewModel currentViewModel;
    protected List<DayForecast> forecast = new ArrayList<>();
    private ForecastViewModel dailyViewModel;

    public FragmentDaily() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            savedInstanceState = new Bundle();
        }
        savedInstanceState.putInt(LAYOUT_KEY, R.layout.fragment_daily);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        forecastAdapter = new ForecastAdapter(forecast, getContext());
        forecastHolder.setAdapter(forecastAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Binding view model to activity rather than a fragment will always ensure for it to survive the orientation change
        currentViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(CurrentViewModel.class);
        currentViewModel.init(this, locationId, location, lat, lon);

        dailyViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(ForecastViewModel.class);
        dailyViewModel.init(this, locationId, location, lat, lon);

        findCurrentWeather();
        findForecast();
    }

    /**
     * Fetch and process current weather
     */
    private void findCurrentWeather() {

        if (currentViewModel.getCurrentWeather().getValue() != null) {

            displayCurrentWeather(currentViewModel.getCurrentWeather().getValue());

            HelpStuff.saveTheCityId(currentViewModel.getCurrentWeather().getValue().getId(), Objects.requireNonNull(getContext()));
        }
    }

    /**
     * Fetch and process the next 6 days forecast
     */
    private void findForecast() {

        if (dailyViewModel.getDailyForecast().getValue() != null) {

            displayForecast(dailyViewModel.getDailyForecast().getValue());
        }
    }

    /**
     * Display the current weather data
     * @param weather Current weather
     */
    @SuppressLint("SetTextI18n")
    private void displayCurrentWeather(CurrentWeather weather) {

        dateField.setText(weather.getDate());

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

            weatherImage.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), icon));
        }
    }

    /**
     * Display the forecast for the next 6 days
     * @param forecastData Forecast weather
     */
    private void displayForecast(Forecast forecastData) {

        forecast = forecastData.getDaysForecast();
        forecastAdapter.update(this.forecast);

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_present_from_bottom);
        super.update(controller);

        /*
         * Very funny that however we tried optimising for some reason notifyDataSetChanged
         * keeps getting called too soon. Lightning speed API and connection?
         */

        // forecastAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateWeather() {

        // For current weather
        findCurrentWeather();
    }

    @Override
    public void updateForecastWeather() {

        // For daily forecast
        findForecast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
         * We have to dispose the disposables held by the repos connected to ViewModels
         * each time the fragment is gone
         */
        currentViewModel.disposeDisposables();
        dailyViewModel.disposeDisposables();
    }
}
