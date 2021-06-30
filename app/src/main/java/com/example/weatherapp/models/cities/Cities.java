package com.example.weatherapp.models.cities;

import com.google.gson.annotations.SerializedName;

public class Cities {

    @SerializedName("name")
    private String citiesName;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    public String getCitiesName() {
        return citiesName;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
