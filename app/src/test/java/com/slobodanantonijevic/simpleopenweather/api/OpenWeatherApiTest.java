package com.slobodanantonijevic.simpleopenweather.api;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.DayForecast;
import com.slobodanantonijevic.simpleopenweather.daily.Forecast;
import com.slobodanantonijevic.simpleopenweather.general.City;
import com.slobodanantonijevic.simpleopenweather.general.MainWeatherData;
import com.slobodanantonijevic.simpleopenweather.general.Weather;
import com.slobodanantonijevic.simpleopenweather.hourly.HourForecast;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi.APP_ID;
import static com.slobodanantonijevic.simpleopenweather.util.ObservableTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OpenWeatherApiTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private OpenWeatherApi apiService;

    private MockWebServer mockWebServer;

    @Before
    public void createService() throws IOException {

        mockWebServer = new MockWebServer();
        apiService = new Retrofit.Builder()
                .baseUrl(String.valueOf(mockWebServer.url("/")))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(OpenWeatherApi.class);

    }

    @After
    public void stopService() throws IOException {

        mockWebServer.shutdown();
    }

    @Test
    public void getCurrentWeather() throws IOException, InterruptedException {

        enqueueResponse("current-weather.json");
        CurrentWeather weather = getValue(apiService.getCurrentWeather(null,"London",null,null));

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/data/2.5/weather?units=metric&APPID=" + APP_ID + "&q=London"));

        assertThat(weather, notNullValue());
        assertEquals(2643743, weather.getId());
        assertEquals("London", weather.getCityName());
    }

    @Test
    public void getForecast() throws IOException, InterruptedException {

        enqueueResponse("forecast-weather.json");
        Forecast forecast = getValue(apiService.getForecast(2643743,null,null,null));

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/data/2.5/forecast/daily?units=metric&APPID=" + APP_ID + "&id=2643743"));

        assertThat(forecast, notNullValue());

        City city = forecast.getCity();
        assertThat(city, notNullValue());
        assertEquals("London", city.getName());
        assertEquals("GB", city.getCountry());
        assertEquals(2643743, city.getId());

        List<DayForecast> daysForecast = forecast.getDaysForecast();
        assertEquals(7, daysForecast.size());

        DayForecast day2 = daysForecast.get(1);
//        assertEquals(1019.99, day2.getPressure()); // deprecated: assertEquals(double, double)
        assertThat(day2, notNullValue());
        assertThat(day2.getPressure(), is(1019.99));
        assertThat(day2.getHumidity(), is(70.0));

        List<Weather> weatherList = day2.getWeather();
        assertThat(weatherList, notNullValue());

        Weather weather  = weatherList.get(0);
        assertThat(weather, notNullValue());
        assertEquals("Clear", weather.getConditions());
    }

    @Test
    public void getHourlyForecast() throws IOException, InterruptedException {

        enqueueResponse("hourly-weather.json");
        HourlyForecast hourlyForecast = getValue(apiService.getHourlyForecast(null,null,"51.51","-0.13"));

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/data/2.5/forecast?units=metric&APPID=" + APP_ID + "&lat=51.51&lon=-0.13"));

        assertThat(hourlyForecast, notNullValue());

        City city = hourlyForecast.getCity();
        assertThat(city, notNullValue());
        assertEquals("London", city.getName());
        assertEquals("GB", city.getCountry());
        assertEquals(2643743, city.getId());

        List<HourForecast> hoursForecast = hourlyForecast.getHoursForecast();
        assertEquals(40, hoursForecast.size());

        HourForecast hour15 = hoursForecast.get(14);
        assertThat(hour15, notNullValue());

        MainWeatherData main = hour15.getConditions();
        assertThat(main.getPressure(), is(1023.64));
        assertThat(main.getHumidity(), is(72.0));
        assertThat(main.getTemp(), is(15.01));

        List<Weather> weatherList = hour15.getWeather();
        assertThat(weatherList, notNullValue());

        Weather weather  = weatherList.get(0);
        assertThat(weather, notNullValue());
        assertEquals("Clouds", weather.getConditions());
    }



    private void enqueueResponse(String fileName) throws IOException {

        enqueueResponse(fileName, Collections.emptyMap());
    }

    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {

        InputStream stream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(stream));
        MockResponse mockResponse= new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {

            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse
                .setBody(source.readString(StandardCharsets.UTF_8)));
    }
}