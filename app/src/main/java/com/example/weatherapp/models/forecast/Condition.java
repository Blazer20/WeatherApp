package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

public class Condition {

    @SerializedName("icon")
    private String urlToImage;

    @SerializedName("text")
    private String text;

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getText() {
        return text;
    }
}
