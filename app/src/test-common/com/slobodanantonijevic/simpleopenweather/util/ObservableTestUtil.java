package com.slobodanantonijevic.simpleopenweather.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ObservableTestUtil {

    static CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static <T> T getValue(Observable<T> obData) throws InterruptedException {

        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        Disposable disposable = obData.subscribe(currentWeather -> data[0] = currentWeather);
        compositeDisposable.add(disposable);

        latch.await(2, TimeUnit.SECONDS);
        compositeDisposable.clear();
        //noinspection unchecked
        return (T) data[0];
    }
}
