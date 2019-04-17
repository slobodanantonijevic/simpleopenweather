package com.slobodanantonijevic.simpleopenweather.hourly;

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

public class HourlyViewModelTest {

    private HourlyRepository repository;
    private HourlyViewModel viewModel;
    private HourlyViewModel viewModelMock;

    @Before
    public void init() {

        viewModelMock = mock(HourlyViewModel.class);
        repository = mock(HourlyRepository.class);
        viewModel = new HourlyViewModel(repository);
    }

    @Test
    public void testNull() {

        assertThat(viewModel.getHourlyWeather(), nullValue());
        verify(repository, never()).getHourlyForecast(anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void getCurrentWeather() {

        LiveData<HourlyForecast> forecast = TestUtil.createHourlyForecast(
                StreamReader.readFromStream("/api-response/hourly-weather.json"));
        when(viewModelMock.getHourlyWeather()).thenReturn(forecast);

        forecast = viewModelMock.getHourlyWeather();
        assertThat(forecast, notNullValue());
    }
}