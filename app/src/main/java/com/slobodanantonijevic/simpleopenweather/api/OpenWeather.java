package com.slobodanantonijevic.simpleopenweather.api;

/**
 * A nearly perfect thread-safe singleton
 * At least I hope... :)
 */



import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

                    /*
                     * In order to use RetroFit with RxJava we need to add RxJava2CallAdapterFactory
                     * Also in order to make network calls asynchronous we need to create it with Schedulers.io()
                     */
                    retrofit = instance = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                            .build();
                }
            }
        }

        return instance;
    }
}
