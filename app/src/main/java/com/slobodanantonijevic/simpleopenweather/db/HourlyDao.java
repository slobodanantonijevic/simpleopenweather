package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.hourly.HourlyForecast;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface HourlyDao {

    @Insert(onConflict = REPLACE)
    void insert(HourlyForecast hourly);

    @Query("SELECT * FROM hourlyforecast WHERE id = :id")
    Observable<HourlyForecast> findById(int id);
}
