package com.slobodanantonijevic.simpleopenweather.di;

import com.slobodanantonijevic.simpleopenweather.daily.FragmentDaily;
import com.slobodanantonijevic.simpleopenweather.hourly.FragmentHourly;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract FragmentHourly contributeHourlyFragment();

    @ContributesAndroidInjector
    abstract FragmentDaily contributeDailyFragment();
}
