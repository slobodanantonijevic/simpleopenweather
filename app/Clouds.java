import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

-----------------------------------com.example.Clouds.java-----------------------------------

        package com.example;

public class Clouds {

@SerializedName("all")
@Expose
private int all;

public int getAll() {
return all;
}

public void setAll(int all) {
this.all = all;
}

}
-----------------------------------com.example.Coord.java-----------------------------------

package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

@SerializedName("lon")
@Expose
private double lon;
@SerializedName("lat")
@Expose
private double lat;

public double getLon() {
return lon;
}

public void setLon(double lon) {
this.lon = lon;
}

public double getLat() {
return lat;
}

public void setLat(double lat) {
this.lat = lat;
}

}
-----------------------------------com.example.Example.java-----------------------------------

package com.example;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

@SerializedName("coord")
@Expose
private Coord coord;
@SerializedName("weather")
@Expose
private List<Weather> weather = null;
@SerializedName("base")
@Expose
private String base;
@SerializedName("main")
@Expose
private Main main;
@SerializedName("visibility")
@Expose
private int visibility;
@SerializedName("wind")
@Expose
private Wind wind;
@SerializedName("clouds")
@Expose
private Clouds clouds;
@SerializedName("dt")
@Expose
private int dt;
@SerializedName("sys")
@Expose
private Sys sys;
@SerializedName("id")
@Expose
private int id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("cod")
@Expose
private int cod;

public Coord getCoord() {
return coord;
}

public void setCoord(Coord coord) {
this.coord = coord;
}

public List<Weather> getWeather() {
return weather;
}

public void setWeather(List<Weather> weather) {
this.weather = weather;
}

public String getBase() {
return base;
}

public void setBase(String base) {
this.base = base;
}

public Main getMain() {
return main;
}

public void setMain(Main main) {
this.main = main;
}

public int getVisibility() {
return visibility;
}

public void setVisibility(int visibility) {
this.visibility = visibility;
}

public Wind getWind() {
return wind;
}

public void setWind(Wind wind) {
this.wind = wind;
}

public Clouds getClouds() {
return clouds;
}

public void setClouds(Clouds clouds) {
this.clouds = clouds;
}

public int getDt() {
return dt;
}

public void setDt(int dt) {
this.dt = dt;
}

public Sys getSys() {
return sys;
}

public void setSys(Sys sys) {
this.sys = sys;
}

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public int getCod() {
return cod;
}

public void setCod(int cod) {
this.cod = cod;
}

}
-----------------------------------com.example.Main.java-----------------------------------

package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

@SerializedName("temp")
@Expose
private double temp;
@SerializedName("pressure")
@Expose
private int pressure;
@SerializedName("humidity")
@Expose
private int humidity;
@SerializedName("temp_min")
@Expose
private double tempMin;
@SerializedName("temp_max")
@Expose
private double tempMax;

public double getTemp() {
return temp;
}

public void setTemp(double temp) {
this.temp = temp;
}

public int getPressure() {
return pressure;
}

public void setPressure(int pressure) {
this.pressure = pressure;
}

public int getHumidity() {
return humidity;
}

public void setHumidity(int humidity) {
this.humidity = humidity;
}

public double getTempMin() {
return tempMin;
}

public void setTempMin(double tempMin) {
this.tempMin = tempMin;
}

public double getTempMax() {
return tempMax;
}

public void setTempMax(double tempMax) {
this.tempMax = tempMax;
}

}
-----------------------------------com.example.Sys.java-----------------------------------

package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

@SerializedName("type")
@Expose
private int type;
@SerializedName("id")
@Expose
private int id;
@SerializedName("message")
@Expose
private double message;
@SerializedName("country")
@Expose
private String country;
@SerializedName("sunrise")
@Expose
private int sunrise;
@SerializedName("sunset")
@Expose
private int sunset;

public int getType() {
return type;
}

public void setType(int type) {
this.type = type;
}

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public double getMessage() {
return message;
}

public void setMessage(double message) {
this.message = message;
}

public String getCountry() {
return country;
}

public void setCountry(String country) {
this.country = country;
}

public int getSunrise() {
return sunrise;
}

public void setSunrise(int sunrise) {
this.sunrise = sunrise;
}

public int getSunset() {
return sunset;
}

public void setSunset(int sunset) {
this.sunset = sunset;
}

}
-----------------------------------com.example.Weather.java-----------------------------------

package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

@SerializedName("id")
@Expose
private int id;
@SerializedName("main")
@Expose
private String main;
@SerializedName("description")
@Expose
private String description;
@SerializedName("icon")
@Expose
private String icon;

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getMain() {
return main;
}

public void setMain(String main) {
this.main = main;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getIcon() {
return icon;
}

public void setIcon(String icon) {
this.icon = icon;
}

}
-----------------------------------com.example.Wind.java-----------------------------------

package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

@SerializedName("speed")
@Expose
private double speed;
@SerializedName("deg")
@Expose
private int deg;

public double getSpeed() {
return speed;
}

public void setSpeed(double speed) {
this.speed = speed;
}

public int getDeg() {
return deg;
}

public void setDeg(int deg) {
this.deg = deg;
}

}