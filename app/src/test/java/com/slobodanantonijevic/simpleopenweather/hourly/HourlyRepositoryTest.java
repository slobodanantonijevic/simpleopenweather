package com.slobodanantonijevic.simpleopenweather.hourly;

import com.slobodanantonijevic.simpleopenweather.RepoTest;
import com.slobodanantonijevic.simpleopenweather.StreamReader;
import com.slobodanantonijevic.simpleopenweather.util.TestUtil;

import org.junit.Test;

import androidx.lifecycle.LiveData;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class HourlyRepositoryTest extends RepoTest {

    private HourlyRepository repo;

    @Override
    public void init() {
        super.init();
        repo = mock(HourlyRepository.class);
    }

    @Test
    public void getHourlyWeather() {

        LiveData<HourlyForecast> forecast = TestUtil.createHourlyForecast(
                StreamReader.readFromStream("/api-response/hourly-weather.json"));

        assertThat(forecast, notNullValue());

        doReturn(forecast).when(repo)
                .getHourlyForecast(2643743, null, null, null);

        repo.getHourlyForecast(2643743, null, null, null);
        testScheduler.triggerActions();

        verify(repo).getHourlyForecast(2643743, null, null, null);
    }
}