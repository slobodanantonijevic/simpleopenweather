package com.slobodanantonijevic.simpleopenweather.api;

/**
 * A nearly perfect thread-safe singleton
 * At least I hope... :)
 */

import javax.inject.Singleton;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class OpenWeather {

    // Access to the variable acts as though it is enclosed in a synchronized block, synchronized on itself
    private static volatile Retrofit retrofit;

    // Dummy object for synchronization
    private static Object dummy = new Object();

    public static final String BASE_URL = "https://api.openweathermap.org/";

    public static Retrofit getRetrofitInstance() {

        Retrofit instance = retrofit;

        if (instance == null) { // Lazy initialization

            synchronized (dummy) { // Synchronization

                instance = retrofit;
                if (instance == null) { // Extra safety

                    retrofit = instance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return instance;
    }
}
