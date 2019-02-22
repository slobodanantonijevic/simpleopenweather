package com.slobodanantonijevic.simpleopenweather.general;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
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

    public static final String LAYOUT_KEY = "inflate_this_layout";

    // Forecast fields & values
    public List<DayForecast> forecast = new ArrayList<>();
    public RecyclerView forecastHolder;

    public CompositeDisposable disposable = new CompositeDisposable();

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
    public void handleRxError(Throwable throwable, String occurrence) {

        // TODO: make some more meaningful error handling
        String message = String.format("%s: Something is not right buddy!", occurrence); // default

        if (throwable instanceof HttpException) {

            HttpException response = (HttpException) throwable;
            int code = response.code();
            message = String.format("%s: We've got HttpException with response code: %d", code);
        }

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {

            disposable.dispose();
        }
    }
}
