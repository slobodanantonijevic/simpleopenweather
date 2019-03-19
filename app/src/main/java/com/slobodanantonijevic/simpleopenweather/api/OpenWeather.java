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

package com.slobodanantonijevic.simpleopenweather.api;

/*
 * A nearly perfect thread-safe singleton
 * At least I hope... :)
 */

import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * We'll deprecate this one as we used it before introduction of Dagger 2 DI
 */
@Deprecated
@Singleton
public class OpenWeather {

    // Access to the variable acts as though it is enclosed in a synchronized block, synchronized on itself
    private static volatile Retrofit retrofit;

    // Dummy object for synchronization
    private static final Object dummy = new Object();

    private static final String BASE_URL = "https://api.openweathermap.org/";

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
