package com.slobodanantonijevic.simpleopenweather.di;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentViewModel;
import com.slobodanantonijevic.simpleopenweather.daily.ForecastViewModel;
import com.slobodanantonijevic.simpleopenweather.hourly.HourlyViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
    //You are able to declare ViewModelProvider.Factory dependency in another module. For example in ApplicationModule.

    @Binds
    @IntoMap
    @ViewModelKey(HourlyViewModel.class)
    abstract ViewModel bindHourlyViewModel(HourlyViewModel hourlyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CurrentViewModel.class)
    abstract ViewModel bindCurrentViewModel(CurrentViewModel currentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel.class)
    abstract ViewModel bindForecastViewModel(ForecastViewModel forecastViewModel);
}
