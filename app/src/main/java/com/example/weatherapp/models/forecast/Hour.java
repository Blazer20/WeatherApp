package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Hour {

    @SerializedName("time")
    private String time;

    @SerializedName("temp_c")
    private double temp;

    @SerializedName("condition")
    private Condition condition;

    public String getTime() {
        return time;
    }

    public double getTemp() {
        return temp;
    }

    public Condition getCondition() {
        return condition;
    }
}
