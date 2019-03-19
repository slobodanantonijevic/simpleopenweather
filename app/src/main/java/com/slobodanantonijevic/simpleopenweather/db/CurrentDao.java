package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.daily.CurrentWeather;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentDao {

    @Insert(onConflict = REPLACE)
    void insert(CurrentWeather currentWeather);

    @Query("SELECT * FROM currentweather WHERE id = :id")
    Observable<CurrentWeather> findById(int id);
}
