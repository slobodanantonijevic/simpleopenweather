package com.slobodanantonijevic.simpleopenweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.hourly.FragmentHourly;

public class ActivityMain extends AppCompatActivity implements FragmentForecast.LocationInterface {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient fusedLocationClient;

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadTheFragment(R.id.navigation_daily);
    }

    /**
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_search_location, null);
        final EditText cityField = view.findViewById(R.id.city);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(R.string.alert_title_location_search);
        alertDialog.setMessage(getString(R.string.alert_text_location_search));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                getString(R.string.alert_button_location_search_manual),
                (dialog, which) -> {

                    HelpStuff.saveTheCity(cityField.getText().toString(), ActivityMain.this);
                    navigation.setSelectedItemId(currentFragmentId);
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.alert_button_location_search_auto),
                (dialog, which) -> {

                    if (ContextCompat.checkSelfPermission(ActivityMain.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted
                        // There should be a handle for shouldShowRequestPermissionRationale here
                        // in other words, if explanation is required, but we will simplify a bit
                        ActivityCompat.requestPermissions(ActivityMain.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_LOCATION_PERMISSION);
                    } else {

                        doTheGeoThingy();
                    }

                    dialog.dismiss();
                });
        alertDialog.show();
    }

    @Override
    public void locationError(String location) {

        if (location.length() > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(R.string.alert_title_location_error);

            String message = getString(R.string.alert_text_location_error)
                    .replace("{location}", location);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                    getString(R.string.alert_button_ok),
                    (dialog, which) -> {

                        popTheSearchDialog();
                        dialog.dismiss();
                    });
            alertDialog.show();
        } else {

            popTheSearchDialog();
        }
    }

    /**
     * We already handle not to call this function without permission
     * so we'll suppress the warning
     */
    @SuppressLint("MissingPermission")
    private void doTheGeoThingy() {

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {

                    if (location != null) {

                        HelpStuff.saveLatAndLon(ActivityMain.this, location);
                        navigation.setSelectedItemId(currentFragmentId);
                    } else {

                        popTheSearchDialog();
                        Toast.makeText(ActivityMain.this,
                                R.string.alert_text_auto_location_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case REQUEST_LOCATION_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    doTheGeoThingy();
                } else {

                    popTheSearchDialog();
                }
                return;
            }
        }

    }
}