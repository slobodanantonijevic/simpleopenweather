package com.slobodanantonijevic.simpleopenweather.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("main")
    @Expose
    private String conditions;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getConditions() {

        return conditions;
    }

    public void setConditions(String main) {

        this.conditions = main;
    }

}
