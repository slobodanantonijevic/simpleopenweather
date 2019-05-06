/*
 * Copyright (C) 2019 Slobodan Antonijević
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.slobodanantonijevic.simpleopenweather.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.slobodanantonijevic.simpleopenweather.daily.Forecast;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * Data Access Object (interface) to handle Daily Forecast weather db querying
 */
@Dao
public interface DailyDao {

    @Insert(onConflict = REPLACE)
    Completable insert(Forecast forecast);

    @Query("SELECT * FROM forecast WHERE id = :id")
    Flowable<Forecast> findById(int id);
}
