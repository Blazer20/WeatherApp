package com.example.weatherapp.models.hourly;

import com.google.gson.annotations.SerializedName;

public class DataH {
    @SerializedName("ts")
    private long date;
    @SerializedName("temp")
    private float temp;
    @SerializedName("weather")
    private Weather weather;

    public long getDate() {
        return this.date;
    }

    public float getTemp() {
        return this.temp;
    }

    public Weather getWeather() {
        return this.weather;
    }
}
