package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

public class Day {

    @SerializedName("maxtemp_c")
    private double maxTemp;

    @SerializedName("mintemp_c")
    private double minTemp;

    @SerializedName("condition")
    private Condition condition;

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public Condition getCondition() {
        return condition;
    }
}
