package com.example.weatherapp.models.daily;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return this.icon;
    }
}
