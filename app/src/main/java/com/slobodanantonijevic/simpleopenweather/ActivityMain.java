package com.slobodanantonijevic.simpleopenweather;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.hourly.FragmentHourly;

public class ActivityMain extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

                // Handle navigation view item clicks here.
                int id = item.getItemId();

                // update the main content by replacing fragments
                loadTheFragment(id);

                return true;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadTheFragment(R.id.navigation_daily);
    }

    /**
     *
     * @param id
     * @return
     */
    private Fragment getFragmentInstance(int id) {

        Fragment fragment = null;
        switch (id) {
            case R.id.navigation_daily:

                fragment = new FragmentDaily();
                break;
            case R.id.navigation_hourly:

                fragment = new FragmentHourly();
                break;
        }

        return fragment;
    }

    /**
     *
     * @param id
     */
    private void loadTheFragment(int id) {

        Fragment fragment = getFragmentInstance(id);
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }
}
