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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.general.City;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;

/**
 * Even though it is rather similar logic to ForecastDaily we'll be keeping it separate
 * so we a complete freedom at updating it in the future.
 */
public class FragmentHourly extends FragmentForecast {

    // Butter Knife
    @BindView(R.id.city) CustomTextView cityField;

    // Forecast fields & values
    private List<HourForecast> forecast = new ArrayList<>();
    private HourlyViewModel viewModel;
    private ForecastAdapter forecastAdapter;

    public FragmentHourly() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {

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
        viewModel.init(this, locationId, location, lat, lon);

        findForecast();
    }

    /**
     * Fetch and process hourly forecast weather
     */
    private void findForecast() {

        if (viewModel.getHourlyWeather().getValue() != null) {

            // save the new current city Id
            HelpStuff.saveTheCityId(viewModel.getHourlyWeather().getValue().getId(), getContext());
            displayForecast(viewModel.getHourlyWeather().getValue());
        }
    }

    /**
     * Display the hourly forecast for the next period
     * @param forecastData Hourly forecast weather
     */
    private void displayForecast(HourlyForecast forecastData) {

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

    @Override
    public void updateWeather() {

        findForecast();
    }

    /**
     *
     * @param city
     */
    private void displayCity(String city) {

        cityField.setText(city);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
         * We have to dispose the disposables held by the repos connected to ViewModels
         * each time the fragment is gone
         */
        viewModel.disposeDisposables();
    }
}
