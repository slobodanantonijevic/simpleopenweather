package com.slobodanantonijevic.simpleopenweather;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.hourly.FragmentHourly;

public class ActivityMain extends AppCompatActivity {

    private int currentFragmentId;
    private BottomNavigationView navigation;

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

        navigation = findViewById(R.id.navigation);
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

            currentFragmentId = id;
        } else {

            popTheSearchDialog();
        }
    }

    /**
     *
     */
    private void popTheSearchDialog() {

        // TODO: Replace with a real dialog.
        // TODO: First one pops the "auto-loc" or "manual" question
        // TODO: Then first one asks permissions and stuff and second one opens the Autocomplete search dialog
        AlertDialog alertDialog = new AlertDialog.Builder(ActivityMain.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        navigation.setSelectedItemId(currentFragmentId);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        //SystemClock.sleep(5000);

        //loadTheFragment(currentFragmentId);

        /*
         * When dialog gets dismissed
         * do loadTheFragment(currentFragmentId);
         */
    }
}
