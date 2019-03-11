package com.slobodanantonijevic.simpleopenweather.di;

import android.app.Application;

import com.slobodanantonijevic.simpleopenweather.App;
import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityMainModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(App app);
}
