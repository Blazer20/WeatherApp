package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Astro {

    @SerializedName("sunrise")
    private String sunrise;

    @SerializedName("sunset")
    private String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }
}
