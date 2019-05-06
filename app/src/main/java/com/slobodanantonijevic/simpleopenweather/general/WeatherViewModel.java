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

package com.slobodanantonijevic.simpleopenweather.general;

import androidx.lifecycle.ViewModel;

import com.slobodanantonijevic.simpleopenweather.api.OpenWeatherApi;

/**
 * Parent ViewModel to serve our ViewModels
 * Currently quite simple but there may be many possibilities to expand in the future
 */
public class WeatherViewModel extends ViewModel {

    public OpenWeatherApi api;
}
