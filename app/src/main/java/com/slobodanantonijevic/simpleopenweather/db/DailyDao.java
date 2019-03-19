package com.slobodanantonijevic.simpleopenweather.db;

import com.slobodanantonijevic.simpleopenweather.daily.Forecast;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Observable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyDao {

    @Insert(onConflict = REPLACE)
    void insert(Forecast forecast);

    @Query("SELECT * FROM forecast WHERE id = :id")
    Observable<Forecast> findById(int id);
}
