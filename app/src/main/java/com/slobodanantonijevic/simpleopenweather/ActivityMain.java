package com.slobodanantonijevic.simpleopenweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.general.FragmentForecast;
import com.slobodanantonijevic.simpleopenweather.general.HelpStuff;
import com.slobodanantonijevic.simpleopenweather.hourly.FragmentHourly;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ActivityMain extends AppCompatActivity implements FragmentForecast.LocationInterface, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    private static final String ACTIVE_FRAGMENT_KEY = "active_fragment";

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FusedLocationProviderClient fusedLocationClient;

    private int currentFragmentId = 0;

    // Butter Knife
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindString(R.string.alert_title_location_search) String alertTitleLocationSearch;
    @BindString(R.string.alert_text_location_search) String alertTextLocationSearch;
    @BindString(R.string.alert_button_location_search_manual) String alertButtonLocationSearchManual;
    @BindString(R.string.alert_button_location_search_auto) String alertButtonLocationSearchAuto;
    @BindString(R.string.alert_title_location_error) String alertTitleLocationError;
    @BindString(R.string.alert_text_location_error) String alertTextLocationError;
    @BindString(R.string.alert_button_ok) String alertButtonOk;
    @BindString(R.string.alert_text_auto_location_error) String alertTextAutoLocationError;

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

        currentFragmentId = checkFragmentFromState(savedInstanceState);
        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(currentFragmentId != 0 ? currentFragmentId : R.id.navigation_daily);
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
        alertDialog.setTitle(alertTitleLocationSearch);
        alertDialog.setMessage(alertTextLocationSearch);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, alertButtonLocationSearchManual,
                (dialog, which) -> {

                    HelpStuff.saveTheCity(cityField.getText().toString(), ActivityMain.this);
                    // no id present will signal the fragment to fetch fresh data
                    HelpStuff.removeTheCityId(ActivityMain.this);

                    navigation.setSelectedItemId(R.id.navigation_daily);
//                    navigation.setSelectedItemId(currentFragmentId);
                    dialog.dismiss();
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, alertButtonLocationSearchAuto,
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

        if (location != null && location.length() > 0) {

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(alertTitleLocationError);

            String message = alertTextLocationError.replace("{location}", location);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, alertButtonOk,
                    (dialog, which) -> {

                        //popTheSearchDialog();
                        navigation.setSelectedItemId(R.id.navigation_search);
                        dialog.dismiss();
                    });
            alertDialog.show();
        } else {

            //popTheSearchDialog();
            navigation.setSelectedItemId(R.id.navigation_search);
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
                        Toast.makeText(ActivityMain.this, alertTextAutoLocationError,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {

            case REQUEST_LOCATION_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    doTheGeoThingy();
                } else {

                    popTheSearchDialog();
                }
            }
        }

    }

    /**
     *
     * @param savedState
     * @return
     */
    private int checkFragmentFromState(Bundle savedState) {

        try {

            return savedState.getInt(ACTIVE_FRAGMENT_KEY);
        } catch (NullPointerException npe) {

            return 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ACTIVE_FRAGMENT_KEY, currentFragmentId);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {

        return dispatchingFragmentInjector;
    }
}