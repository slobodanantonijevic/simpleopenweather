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
package com.slobodanantonijevic.simpleopenweather.di;

import android.app.Application;

import androidx.room.Room;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.CurrentDao;
import com.slobodanantonijevic.simpleopenweather.db.DailyDao;
import com.slobodanantonijevic.simpleopenweather.db.HourlyDao;
import com.slobodanantonijevic.simpleopenweather.db.WeatherDb;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi.BASE_URL;
import static com.slobodanantonijevic.simpleopenweather.db.WeatherDb.DB_NAME;

/**
 * Module to provide the Dagger all the necessary elements for our data flow
 * Such as Retrofit built client to fetch the fresh data, and Room DB and DAOs for data persistence
 */
@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    OpenWeatherApi provideWeatherApi() {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
                .create(OpenWeatherApi.class);
    }

    @Singleton
    @Provides
    WeatherDb provideDb(Application app) {

        return Room.databaseBuilder(app, WeatherDb.class, DB_NAME).build();
    }

    @Singleton
    @Provides
    HourlyDao provideHourlyDao(WeatherDb db) {

        return db.hourlyDao();
    }

    @Singleton
    @Provides
    CurrentDao provideCurrentDao(WeatherDb db) {

        return db.currentDao();
    }

    @Singleton
    @Provides
    DailyDao provideDailyDao(WeatherDb db) {

        return db.dailyDao();
    }
}
