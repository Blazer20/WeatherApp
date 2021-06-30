package com.example.weatherapp.models.cities;

import com.google.gson.annotations.SerializedName;

public class Terms {

    @SerializedName("value")
    private String value;

    public String getValue() {
        return value;
    }
}
