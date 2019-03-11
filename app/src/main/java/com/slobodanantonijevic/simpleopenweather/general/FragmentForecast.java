package com.slobodanantonijevic.simpleopenweather.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.slobodanantonijevic.simpleopenweather.R;
import com.slobodanantonijevic.simpleopenweather.daily.DayForecast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentForecast extends Fragment implements Repository.LocationErrorInterface, Repository.UpdateWeatherInterface {

    protected static final int CURRENT_WEATHER = 0;
    protected static final int DAILY_WEATHER = 1;
    public static final int HOURLY_WEATHER = 2;

    protected static final String LAYOUT_KEY = "inflate_this_layout";

    // Butter Knife Unbinder
    protected Unbinder unbinder;

    // Forecast fields & values
    protected List<DayForecast> forecast = new ArrayList<>();

    // Butter Knife
    @BindView(R.id.forecastHolder) protected RecyclerView forecastHolder;

    protected String location;
    protected String lat;
    protected String lon;

    protected CompositeDisposable disposable = new CompositeDisposable();

    private LocationInterface callback;

    public interface LocationInterface {

        public void locationError(String location);
    }

    public FragmentForecast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layout = savedInstanceState.getInt(LAYOUT_KEY);
        View rootView = inflater.inflate(layout, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        forecastHolder.setLayoutManager(layoutManager);
        forecastHolder.setItemAnimator(new DefaultItemAnimator());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        location = HelpStuff.retrieveSavedCity(Objects.requireNonNull(getContext()));
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
    protected void update(LayoutAnimationController controller) {

        forecastHolder.setLayoutAnimation(controller);

        Objects.requireNonNull(forecastHolder.getAdapter()).notifyDataSetChanged();
        forecastHolder.scheduleLayoutAnimation();
    }

    /**
     *
     * @param throwable
     */
    @SuppressLint("DefaultLocale")
    protected void handleRxError(Throwable throwable, int occurrence) {

        // TODO: make some more meaningful error handling
        String message = "WEATHER: Something is not right buddy!"; // default

        if (throwable instanceof HttpException && occurrence != DAILY_WEATHER) {

            HttpException response = (HttpException) throwable;
            Log.wtf("RETRO ERROR", response.response().raw().request().url().toString());
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

        if (unbinder != null) {

            unbinder.unbind();
        };
    }

    @Override
    public void locationError(Throwable throwable, int occurrence) {

        handleRxError(throwable, occurrence);
    }

    @Override
    public void updateWeather() { }
}
