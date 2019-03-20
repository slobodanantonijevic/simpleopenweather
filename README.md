# SimpleOpenWeather
Simple Weather App that I have built for the purpose of displaying to junior devs on a local course the relatively basic usage of ConstraintLayout, RecyclerView, Scalable Vector Drawables, Retrofit, RxJava, Dagger2, Butter Knife & Architecture Components (ViewModel, LiveData and Room).

The app uses free account of [OpenWeatherMap API](https://openweathermap.org/api)

![Apache License](https://img.shields.io/badge/license-Apache--2.0-blue.svg) ![api 16+](https://img.shields.io/badge/API-16%2B-green.svg) ![Tech used](https://img.shields.io/badge/tech-ConstraintLayout%20%7C%20SVG%20%7C%20RxJava%20%7C%20Retrofit%20%7C%20Dagger%20%7C%20Butter%20Knife%20%7C%20Architecture%20Components%20%7C%20AndroidX-red.svg)

## Main technology used
- **ConstraintLayout**: A layout that allows us to create large and complex layouts with a flat view hierarchy (no nested view groups). 
- **Scalable Vector Drawables**: SVG vector graphics that allow significant reduction of the APK and app size, compared to bitmap resources.
- **Retrofit 2.0**: A type-safe HTTP client, that is used to comunicate with the designated API.
- **RxJava**: A Java VM implementation of Reactive Extensions. For composing asynchronous sequences of code, and implementing a reactive/asynchronous programming concept.
- **Dagger 2**: A dependency injection framework. We use it to implement a design pattern with minimal burden of writing the boilerplate code.
- **Butter Knife**: View and method binding for Android views. To reduce the boilerplate code neede to locate our fields and set methods over them.
- **Architecture Components**: A collection of libraries that help us design robust, testable, and maintainable apps. Mainly used for data persistance and lifecycle awareness.
  - **ViewModel**: Class is designed to store and manage UI-related data in a lifecycle conscious way. Used store the UI related data so it survives the device rotation
  - **LiveData**: An observable data holder that is lifecycle aware. Used in order to honour lifecycle of other Android components.
  - **Room**: A SQLite object mapping library. Used to Avoid boilerplate code and easily convert SQLite table data to Java objects.
- **AndroidX**: The open-source project that is a major improvement to the original Android Support Library.

## Main goals:
1. Introduce local junior devs to the latest trends in Android tech
2. Introduce them to pretty much 'must-use' libraries such as Retrofit, Rx, Dagger etc.
3. Discus the importance of embracing these technologies. Reducing boilerplate, introducing reactive patterns, and persisting the data with lowering the amount of bandwidth consumption.
4. Show how to improve the UX feel with minor basic animations.
5. Display the examples how to combine the above mentioned technologies in a single app project
6. Discus about further possible improvements that can be done 

## Important
In order for the app to work you need to provide your own **APP ID** from https://openweathermap.org, in ```api/OpenWeatherApi.java```. A free plan will work with this app. 

## Screenshots
<img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/Screenshot_1553086121.png" width="200"> <img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/Screenshot_1553086127.png" width="200"> <img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/Screenshot_1553086166.png" width="200"> <img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/Screenshot_1553086176.png" width="200">
<img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/Screenshot_1553086137.png" width="500">

## Demo
<img src="https://raw.githubusercontent.com/slobodanantonijevic/SimpleOpenWeather/master/screenshots/gif_demo.gif" width="200">

```
                                 Apache License
                           Version 2.0, January 2004
                        http://www.apache.org/licenses/

   Copyright 2018 Slobodan Antonijević

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
