package com.slobodanantonijevic.simpleopenweather.daily;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
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

import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

        unbinder = ButterKnife.bind(this, rootView);

        forecastAdapter = new ForecastAdapter(forecast, getContext());
        forecastHolder.setAdapter(forecastAdapter);

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
     */
    private void findCurrentWeather() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Observable<CurrentWeather> call = api.getCurrentWeather(location, lat, lon);

        Disposable current = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentWeather -> {

                    String date = HelpStuff
                            .time(currentWeather.getUnixDate(), "EEE, LLL dd");
                    currentWeather.setDate(date);
                    displayCurrentWeather(currentWeather);
                }, throwable -> handleRxError(throwable, CURRENT_WEATHER));  // onError

        disposable.add(current);
    }

    /**
     *
     */
    private void findForecast() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Observable<Forecast> call = api.getForecast(location, lat, lon);

        Disposable forecast = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayForecast, // simple method reference
                        throwable -> handleRxError(throwable, DAILY_WEATHER)); // onError

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
     *
     * @param forecastData
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
}
