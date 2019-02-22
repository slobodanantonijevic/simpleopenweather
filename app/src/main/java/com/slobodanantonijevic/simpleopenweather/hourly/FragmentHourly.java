package com.slobodanantonijevic.simpleopenweather.hourly;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.general.City;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.widget.CustomTextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Even though it is rather similar logic to ForecastDaily we'll be keeping it separate
 * so we a complete freedom at updating it in the future.
 */
public class FragmentHourly extends FragmentForecast {

    // Forecast fields & values
    public List<HourForecast> forecast = new ArrayList<>();
    private City city;
    private ForecastAdapter forecastAdapter;

    private CustomTextView cityField;

    public FragmentHourly() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null) {

            savedInstanceState = new Bundle();
        }
        savedInstanceState.putInt(LAYOUT_KEY, R.layout.fragment_hourly);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        forecastHolder = rootView.findViewById(R.id.forecastHolder);

        forecastAdapter = new ForecastAdapter(forecast, getContext());
        forecastHolder.setAdapter(forecastAdapter);

        int resId = R.anim.layout_anim_present_from_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        forecastHolder.setLayoutAnimation(animation);

        locateHeaderFields(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findForecast();
    }

    /**
     *
     */
    private void findForecast() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Observable<HourlyForecast> call = api.getHourlyForecast("London, UK");

        Disposable hourly = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayForecast, // simple method reference
                        throwable -> handleRxError(throwable, "DAILY WEATHER")); // onError

//                Subscribe could have been done with lambda too
//                but no point to it as we only have a single method call
//                and it is more elegant to use a simple method reference
//                .subscribe(hourlyForecast -> {
//
//                    displayForecast(hourlyForecast);
//                });

        disposable.add(hourly);
    }

    /**
     *
     * @param forecastData
     */
    private void displayForecast(HourlyForecast forecastData) {

        forecast = forecastData.getHoursForecast();
        city = forecastData.getCity();
        displayCity(city.cityAndCountry());
        forecastAdapter.update(this.forecast);

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_present_from_right);
        super.update(controller);
    }

    /**
     *
     * @param city
     */
    private void displayCity(String city) {

        cityField.setText(city);
    }

    /**
     *
     * @param rootView
     */
    private void locateHeaderFields(View rootView) {

        cityField = rootView.findViewById(R.id.city);
    }
}
