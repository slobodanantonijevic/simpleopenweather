package com.slobodanantonijevic.simpleopenweather.di;

import com.slobodanantonijevic.simpleopenweather.ActivityMain;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityMainModule {

    @ContributesAndroidInjector(modules = FragmentsModule.class)
    abstract ActivityMain contributeMainActivity();
}