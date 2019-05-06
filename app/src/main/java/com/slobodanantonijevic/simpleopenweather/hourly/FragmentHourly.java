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

package com.slobodanantonijevic.simpleopenweather.hourly;

import android.content.Context;
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
import androidx.lifecycle.ViewModelProviders;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.general.Animations;
import com.slobodanantonijevic.simpleopenweather.general.City;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Even though it is rather similar logic to ForecastDaily we'll be keeping it separate
 * so we a complete freedom at updating it in the future.
 */
public class FragmentHourly extends FragmentForecast {

    private static final String TAG = FragmentDaily.class.getSimpleName();

    // Butter Knife
    @BindView(R.id.refreshWeather) ImageButton refreshWeather;
    @BindView(R.id.city) TextView cityField;

    // Forecast fields & values
    private List<HourForecast> forecast = new ArrayList<>();
    private HourlyViewModel viewModel;
    private ForecastAdapter forecastAdapter;

    public FragmentHourly() {
        // Required empty public constructor
    }

    /**
     *
     */
    @OnClick(R.id.refreshWeather)
    protected void refreshWeather() {

        getFreshForecastWeather(locationId, location);
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            savedInstanceState = new Bundle();
        }
        savedInstanceState.putInt(LAYOUT_KEY, R.layout.fragment_hourly);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        forecastAdapter = new ForecastAdapter(forecast, getContext());
        forecastHolder.setAdapter(forecastAdapter);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_present_from_right);
        forecastHolder.setLayoutAnimation(animation);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Binding view model to activity rather than a fragment will always ensure for it to survive the orientation change
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory)
                .get(HourlyViewModel.class);

        listenToHourlyForecast();
    }

    /**
     *
     * @param id
     * @param name
     */
    private void getFreshForecastWeather(Integer id, String name) {

        Animations.rotate(getContext(), refreshWeather);
        refreshWeather.setEnabled(false);
        disposable.add(viewModel.getFreshWeather(id, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        hourlyForecast -> {

                            if (locationId == null) {

                                locationId = hourlyForecast.getCity().getId();
                                location = hourlyForecast.getCity().getName();

                                HelpStuff.saveTheCityId(locationId, Objects.requireNonNull(getContext()));
                            }

                            hourlyForecast.setId(hourlyForecast.getCity().getId());

                            listenToHourlyForecast();
                            updateTheForecastWeather(hourlyForecast);

                        },
                        error -> handleRxError(error, DAILY_WEATHER)));
    }

    /**
     *
     */
    private void listenToHourlyForecast() {

        disposable.add(viewModel.hourlyForecast(locationId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        hourlyForecast -> {

                            if (hourlyForecast != null) {

                                updateTheHourlyWeatherUi(hourlyForecast);
                            } else {

                                getFreshForecastWeather(locationId, location);
                            }
                        },
                        error -> handleRxError(error, HOURLY_WEATHER)));
    }

    /**
     *
     * @param forecast
     */
    private void updateTheForecastWeather(HourlyForecast forecast) {

        disposable.add(viewModel.updateWeatherData(forecast)
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
     * Display the hourly forecast for the next period
     * @param forecastData Hourly forecast weather
     */
    private void updateTheHourlyWeatherUi(HourlyForecast forecastData) {

        try {

            forecast = forecastData.getHoursForecast();
            City city = forecastData.getCity();
            displayCity(city.cityAndCountry());
            forecastAdapter.update(this.forecast);

            final LayoutAnimationController controller =
                    AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_present_from_right);
            super.update(controller);
        } catch (NullPointerException npe) {

            // Do nothing wait for the data to come
        }
    }

    /**
     *
     * @param city
     */
    private void displayCity(String city) {

        cityField.setText(city);
    }
}
