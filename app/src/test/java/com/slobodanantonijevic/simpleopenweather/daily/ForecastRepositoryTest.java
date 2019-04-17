package com.slobodanantonijevic.simpleopenweather.daily;

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

public class ForecastRepositoryTest extends RepoTest {

    private ForecastRepository repo;

    @Override
    public void init() {
        super.init();
        repo = mock(ForecastRepository.class);
    }

    @Test
    public void getForecast() {

        LiveData<Forecast> forecast = TestUtil.createForecast(
                StreamReader.readFromStream("/api-response/forecast-weather.json"));

        assertThat(forecast, notNullValue());

        doReturn(forecast).when(repo)
                .getDailyForecast(2643743, null, null, null);

        repo.getDailyForecast(2643743, null, null, null);
        testScheduler.triggerActions();

        verify(repo).getDailyForecast(2643743, null, null, null);
    }

}