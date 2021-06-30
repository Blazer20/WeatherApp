package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("location")
    private Location myLocation;

    @SerializedName("current")
    private Current myCurrent;

    @SerializedName("forecast")
    private Forecast myForecast;

    public Location getMyLocation() {
        return myLocation;
    }

    public Current getMyCurrent() {
        return myCurrent;
    }

    public Forecast getMyForecast() {
        return myForecast;
    }
}
