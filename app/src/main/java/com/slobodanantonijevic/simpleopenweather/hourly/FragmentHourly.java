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
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import dagger.android.support.AndroidSupportInjection;

/**
 * Even though it is rather similar logic to ForecastDaily we'll be keeping it separate
 * so we a complete freedom at updating it in the future.
 */
public class FragmentHourly extends FragmentForecast {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

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

        AndroidSupportInjection.inject(this); // Has to be done after the activity is created

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HourlyViewModel.class);
        viewModel.init(this, location, lat, lon);

        findForecast();

    }

    /**
     *
     */
    private void findForecast() {

        if (viewModel.getHourlyWeather().getValue() != null) {

            displayForecast(viewModel.getHourlyWeather().getValue());
        }
    }

    /**
     *
     * @param forecastData
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

        viewModel.disposeDisposables();
    }
}
