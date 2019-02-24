package com.slobodanantonijevic.simpleopenweather.general;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.daily.DayForecast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForecast extends Fragment {

    public static final int CURRENT_WEATHER = 0;
    public static final int DAILY_WEATHER = 1;
    public static final int HOURLY_WEATHER = 2;

    public static final String LAYOUT_KEY = "inflate_this_layout";

    // Forecast fields & values
    public List<DayForecast> forecast = new ArrayList<>();
    public RecyclerView forecastHolder;

    public String location;
    public String lat;
    public String lon;

    public CompositeDisposable disposable = new CompositeDisposable();

    private LocationInterface callback;

    public interface LocationInterface {

        public void locationError(String location);
    }

    public FragmentForecast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layout = savedInstanceState.getInt(LAYOUT_KEY);
        View rootView = inflater.inflate(layout, container, false);

        forecastHolder = rootView.findViewById(R.id.forecastHolder);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        forecastHolder.setLayoutManager(layoutManager);
        forecastHolder.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        location = HelpStuff.retrieveSavedCity(getContext());
        if (location == null) {

            String[] coords = HelpStuff.retrieveSavedCoords(getContext());
            lat = coords[0];
            lon = coords[1];
        }
    }

    @Override
    public void onAttach(Context context) {

        callback = (LocationInterface) context;
        super.onAttach(context);
    }

    /**
     *
     * @param controller
     */
    public void update(LayoutAnimationController controller) {

        forecastHolder.setLayoutAnimation(controller);

        forecastHolder.getAdapter().notifyDataSetChanged();
        forecastHolder.scheduleLayoutAnimation();
    }

    /**
     *
     * @param throwable
     */
    public void handleRxError(Throwable throwable, int occurrence) {

        // TODO: make some more meaningful error handling
        String message = "WEATHER: Something is not right buddy!"; // default

        if (throwable instanceof HttpException && occurrence != DAILY_WEATHER) {

            HttpException response = (HttpException) throwable;
            int code = response.code();

            switch (code) {

                case 400:
                case 404:

                    callback.locationError(location);
                    break;
                default:

                    message = String.format("We've got HttpException with response code: %d", code);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
