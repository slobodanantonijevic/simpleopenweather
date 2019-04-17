package com.slobodanantonijevic.simpleopenweather.daily;

import com.slobodanantonijevic.simpleopenweather.RepoTest;
import com.slobodanantonijevic.simpleopenweather.StreamReader;
import com.slobodanantonijevic.simpleopenweather.util.TestUtil;

import org.junit.Test;

import androidx.lifecycle.LiveData;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CurrentWeatherRepositoryTest extends RepoTest {

    private CurrentWeatherRepository repo;

    @Override
    public void init() {
        super.init();
        repo = mock(CurrentWeatherRepository.class);
    }

    @Test
    public void getCurrentWeather() {

        LiveData<CurrentWeather> cw = TestUtil.createCurrentWeather(
                StreamReader.readFromStream("/api-response/current-weather.json"));

        assertThat(cw, notNullValue());

        doReturn(cw).when(repo)
                .getCurrentWeather(2643743, null, null, null);

        repo.getCurrentWeather(2643743, null, null, null);
        testScheduler.triggerActions();

        verify(repo).getCurrentWeather(2643743, null, null, null);
    }
}