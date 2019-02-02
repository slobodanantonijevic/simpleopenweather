package com.slobodanantonijevic.simpleopenweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.model.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.model.Forecast;
import com.slobodanantonijevic.simpleopenweather.model.HourlyForecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_daily:

                    Toast.makeText(MainActivity.this, "Daily", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_hourly:

                    Toast.makeText(MainActivity.this, "Hourly", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void tryRetrofit() {

        OpenWeatherApi api = OpenWeather.getRetrofitInstance().create(OpenWeatherApi.class);

        Call<CurrentWeather> call = api.getCurrentWeather("London,UK");

        Log.wtf("URL_CALLED", call.request().url() + "");

        call.enqueue(new Callback<CurrentWeather>() {

            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {

                CurrentWeather weather = response.body();

                Log.wtf("RESPONSE_CODE", response.code() + "");
                Log.wtf("RESPONSE_CITY", weather.getCityName());
                Log.wtf("RESPONSE_DATE", weather.getDate() + "");
                Log.wtf("RESPONSE_ID", weather.getId() + "");
                Log.wtf("RESPONSE_TEMP", weather.getMain().getTemp() + "");
                Log.wtf("RESPONSE_HUMIDITY", weather.getMain().getHumidity() + "");
                Log.wtf("RESPONSE_PRESSURE", weather.getMain().getPressure() + "");
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {

                Toast.makeText(MainActivity.this, "A FUCKUP BUDDY!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
