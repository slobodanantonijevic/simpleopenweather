package com.slobodanantonijevic.simpleopenweather.di;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi.BASE_URL;

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
}
