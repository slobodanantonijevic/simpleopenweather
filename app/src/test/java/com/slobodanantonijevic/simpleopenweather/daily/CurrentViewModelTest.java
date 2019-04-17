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

public class CurrentViewModelTest {

    private CurrentWeatherRepository repository;
    private CurrentViewModel viewModel;
    private CurrentViewModel viewModelMock;

    @Before
    public void init() {

        viewModelMock = mock(CurrentViewModel.class);
        repository = mock(CurrentWeatherRepository.class);
        viewModel = new CurrentViewModel(repository);
    }

    @Test
    public void testNull() {

        assertThat(viewModel.getCurrentWeather(), nullValue());
        verify(repository, never()).getCurrentWeather(anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void getCurrentWeather() {

        LiveData<CurrentWeather> cw = TestUtil.createCurrentWeather(
                StreamReader.readFromStream("/api-response/current-weather.json"));
        when(viewModelMock.getCurrentWeather()).thenReturn(cw);

        cw = viewModelMock.getCurrentWeather();
        assertThat(cw, notNullValue());
    }
}