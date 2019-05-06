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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.Animations;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FragmentDaily extends FragmentForecast {

    private static final String TAG = FragmentDaily.class.getSimpleName();

    private ForecastAdapter forecastAdapter;

    // Butter Knife
    @BindView(R.id.refreshWeather) ImageButton refreshWeather;
    @BindView(R.id.date) TextView dateField;
    @BindView(R.id.city) TextView cityField;
    @BindView(R.id.currentTemperature) TextView temperatureField;
    @BindView(R.id.pressure) TextView pressureField;
    @BindView(R.id.humidity) TextView humidityField;
    @BindView(R.id.wind) TextView windField;
    @BindView(R.id.minTemp) TextView minTempField;
    @BindView(R.id.maxTemp) TextView maxTempField;
    @BindView(R.id.weatherIcon) AppCompatImageView weatherImage;

    // ViewModels' data
    private CurrentViewModel currentViewModel;
    protected List<DayForecast> forecast = new ArrayList<>();
    private ForecastViewModel dailyViewModel;

    public FragmentDaily() {

        // Required empty public constructor
    }

    /**
     *
     */
    @OnClick(R.id.refreshWeather)
    protected void refreshWeather() {

        getFreshCurrentWeather(locationId, location);
        getFreshForecastWeather(locationId, location);
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

        dailyViewModel = ViewModelProviders.of(getActivity(), viewModelFactory)
                .get(ForecastViewModel.class);

        listenToCurrentWeather();
        listenToForecast();

        if (locationId == null || locationId < -1) {

            getFreshCurrentWeather(locationId, location);
            getFreshForecastWeather(locationId, location);
        }
    }

    /**
     *
     * @param id
     * @param name
     */
    private void getFreshCurrentWeather(Integer id, String name) {

        Animations.rotate(getContext(), refreshWeather);
        refreshWeather.setEnabled(false);
        disposable.add(currentViewModel.getFreshWeather(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        currentWeather -> {

                            // This (locationId == null) means we have the new city and need new disposables
                            if (locationId == null) {

                                locationId = currentWeather.getId();
                                location = currentWeather.getCityName();

                                HelpStuff.saveTheCityId(locationId, Objects.requireNonNull(getContext()));
                            }
                            listenToCurrentWeather();
                            updateTheCurrentWeather(currentWeather);

                        },
                        error -> handleRxError(error, CURRENT_WEATHER) ));
    }

    /**
     *
     */
    private void getFreshForecastWeather(Integer id, String name) {

        disposable.add(dailyViewModel.getFreshWeather(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        forecastWeather -> {

                            if (locationId == null) {

                                locationId = forecastWeather.getCity().getId();
                                location = forecastWeather.getCity().getName();

                                HelpStuff.saveTheCityId(locationId, Objects.requireNonNull(getContext()));
                            }

                            forecastWeather.setId(forecastWeather.getCity().getId());

                            listenToForecast();
                            updateTheForecastWeather(forecastWeather);

                        },
                        error -> handleRxError(error, DAILY_WEATHER)));
    }

    /**
     *
     */
    private void listenToCurrentWeather() {

        disposable.add(currentViewModel.currentWeather(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        currentWeather -> {

                            if (currentWeather != null) {

                                updateTheCurrentWeatherUi(currentWeather);
                            } else {

                                callback.locationError(location);
                            }
                        },
                        error -> handleRxError(error, CURRENT_WEATHER)));
    }

    /**
     *
     */
    private void listenToForecast() {

        disposable.add(dailyViewModel.dailyForecast(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        forecastWeather -> {

                            if (forecastWeather != null) {

                                updateTheForecastWeatherUi(forecastWeather);
                            }
                        },
                        error -> handleRxError(error, DAILY_WEATHER)));
    }

    /**
     *
     * @param weather
     */
    private void updateTheCurrentWeather(CurrentWeather weather) {

        disposable.add(currentViewModel.updateWeatherData(weather)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                                refreshWeather.clearAnimation();
                                refreshWeather.setEnabled(true);
                        },
                        error -> Log.e(TAG, "Unable to update weather", error)));
    }

    /**
     *
     * @param forecast
     */
    private void updateTheForecastWeather(Forecast forecast) {

        disposable.add(dailyViewModel.updateWeatherData(forecast)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> { },
                        error -> Log.e(TAG, "Unable to update weather", error)));
    }

    /**
     * Display the current weather data
     * @param weather Current weather
     */
    @SuppressLint("SetTextI18n")
    private void updateTheCurrentWeatherUi(CurrentWeather weather) {

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
    private void updateTheForecastWeatherUi(Forecast forecastData) {

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
}
