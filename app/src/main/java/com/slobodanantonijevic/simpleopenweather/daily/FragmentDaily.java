package com.slobodanantonijevic.simpleopenweather.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.widget.CustomTextView;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentDaily extends FragmentForecast {

    public ForecastAdapter forecastAdapter;

    /**
     * Generally we did not have to put them as globals but since we'll add a refresh functionality
     * preventing multiple findViewById(s) (on each refresh), seems like a smart idea
     */
    private CustomTextView dateField;
    private CustomTextView cityField;
    private CustomTextView temperatureField;
    private CustomTextView pressureField;
    private CustomTextView humidityField;
    private CustomTextView windField;
    private CustomTextView minTempField;
    private CustomTextView maxTempField;
    private AppCompatImageView weatherImage;

    public FragmentDaily() {

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
        savedInstanceState.putInt(LAYOUT_KEY, R.layout.fragment_daily);
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView forecastHolder = rootView.findViewById(R.id.forecastHolder);
        forecastAdapter = new ForecastAdapter(forecast, getContext());
        forecastHolder.setAdapter(forecastAdapter);

        locateCurrentWeatherFields(rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findCurrentWeather();
        findForecast();
    }

    /**
     *
     * @param rootView
     */
    private void locateCurrentWeatherFields(View rootView) {

        dateField = rootView.findViewById(R.id.date);
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

        Observable<CurrentWeather> call = api.getCurrentWeather("London,UK");

        Disposable current = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {

                    String date = HelpStuff
                            .time(currentWeather.getUnixDate(), "EEE, LLL dd");
                    currentWeather.setDate(date);
                    displayCurrentWeather(currentWeather);
                }, throwable -> handleRxError(throwable, "CURRENT WEATHER"));  // onError

        disposable.add(current);
    }

    /**
     *
     */
    private void findForecast() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Observable<Forecast> call = api.getForecast("London,UK");

        Disposable forecast = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayForecast, // simple method reference
                        throwable -> handleRxError(throwable, "DAILY WEATHER")); // onError

//                Subscribe could have been done with lambda too
//                but no point to it as we only have a single method call
//                and it is more elegant to use a simple method reference
//                .subscribe(forecastWeather -> {
//
//                    displayForecast(forecastWeather);
//                });

        disposable.add(forecast);
    }

    /**
     *
     * @param weather
     */
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

            weatherImage.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
        }
    }

    /**
     *
     * @param forecastData
     */
    private void displayForecast(Forecast forecastData) {

        forecast = forecastData.getDaysForecast();
        forecastAdapter.update(this.forecast);

        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_present_from_bottom);
        super.update(controller);

        /**
         * Very funny that however we tried optimising for some reason notifyDataSetChanged
         * keeps getting called too soon. Lightning speed API and connection?
         */

        // forecastAdapter.notifyDataSetChanged();
    }
}
