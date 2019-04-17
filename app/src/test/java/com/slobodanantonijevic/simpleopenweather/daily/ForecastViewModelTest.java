package com.slobodanantonijevic.simpleopenweather.daily;

import com.slobodanantonijevic.simpleopenweather.StreamReader;
import com.slobodanantonijevic.simpleopenweather.util.TestUtil;

import org.junit.Before;
import org.junit.Test;

import androidx.lifecycle.LiveData;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForecastViewModelTest {

    private ForecastRepository repository;
    private ForecastViewModel viewModel;
    private ForecastViewModel viewModelMock;

    @Before
    public void init() {

        viewModelMock = mock(ForecastViewModel.class);
        repository = mock(ForecastRepository.class);
        viewModel = new ForecastViewModel(repository);
    }

    @Test
    public void testNull() {

        assertThat(viewModel.getDailyForecast(), nullValue());
        verify(repository, never()).getDailyForecast(anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void getCurrentWeather() {

        LiveData<Forecast> forecast = TestUtil.createForecast(
                StreamReader.readFromStream("/api-response/forecast-weather.json"));
        when(viewModelMock.getDailyForecast()).thenReturn(forecast);

        forecast = viewModelMock.getDailyForecast();
        assertThat(forecast, notNullValue());
    }
}