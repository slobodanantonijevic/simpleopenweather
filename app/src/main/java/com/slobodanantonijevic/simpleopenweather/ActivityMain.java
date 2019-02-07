package com.slobodanantonijevic.simpleopenweather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeather;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;
import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Handle navigation view item clicks here.
            int id = item.getItemId();
            // update the main content by replacing fragments
            Fragment fragment = getFragmentInstance(id);
            if (fragment != null) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, fragment)
                        .commit();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private Fragment getFragmentInstance(int id) {

        Fragment fragment = null;
        switch (id) {
            case R.id.navigation_daily:

                fragment = new FragmentDaily();
            case R.id.navigation_hourly:

                Toast.makeText(ActivityMain.this, "Hourly", Toast.LENGTH_SHORT).show();
        }

        return fragment;
    }
}
