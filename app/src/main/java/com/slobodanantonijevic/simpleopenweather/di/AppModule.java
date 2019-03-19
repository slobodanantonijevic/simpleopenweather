package com.slobodanantonijevic.simpleopenweather.di;

import android.app.Application;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;
import com.slobodanantonijevic.simpleopenweather.db.CurrentDao;
import com.slobodanantonijevic.simpleopenweather.db.DailyDao;
import com.slobodanantonijevic.simpleopenweather.db.HourlyDao;
import com.slobodanantonijevic.simpleopenweather.db.WeatherDb;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi.BASE_URL;
import static com.slobodanantonijevic.simpleopenweather.db.WeatherDb.DB_NAME;
import static com.slobodanantonijevic.simpleopenweather.db.WeatherDb.MIGRATION_1_2;

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

        return Room.databaseBuilder(app, WeatherDb.class, DB_NAME).addMigrations(MIGRATION_1_2).build();
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
