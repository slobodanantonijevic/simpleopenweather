/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanantonijevic.simpleopenweather.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.slobodanantonijevic.simpleopenweather.R;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.HttpException;

/**
 * A custom fragment we want to extend our Fragments from, so that we reduce the code used in all
 * of our fragments and limit it to one place
 */
public class FragmentForecast extends Fragment {

    protected static final int CURRENT_WEATHER = 1;
    protected static final int DAILY_WEATHER = 2;
    public static final int HOURLY_WEATHER = 3;

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    protected static final String LAYOUT_KEY = "inflate_this_layout";

    // Butter Knife Unbinder
    private Unbinder unbinder;

    // Butter Knife
    @BindView(R.id.forecastHolder) protected RecyclerView forecastHolder;

    protected Integer locationId = null;
    protected String location = null;

    protected CompositeDisposable disposable = new CompositeDisposable();

    protected LocationInterface callback;

    /**
     * Interface to message Main Activity to take some action
     */
    public interface LocationInterface {

        void locationError(String location);
    }

    public FragmentForecast() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        AndroidSupportInjection.inject(this); // Has to be done after the activity is created
        locationId = HelpStuff.retrieveSavedCityId(Objects.requireNonNull(getContext()));
        location = HelpStuff.retrieveSavedCity(Objects.requireNonNull(getContext()));
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
    public void onAttach(@NonNull Context context) {

        callback = (LocationInterface) context;
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();

        disposable.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (unbinder != null) {

            unbinder.unbind();
        };

        disposable.dispose();
    }
}
