package com.example.weatherapp.models.daily;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class DataD {
    @SerializedName("datetime")
    private Date date;
    @SerializedName("max_temp")
    private float max_temp;
    @SerializedName("min_temp")
    private float min_temp;
    @SerializedName("weather")
    private Weather weather;

    public Date getDate() {
        return this.date;
    }

    public float getMax_temp() {
        return this.max_temp;
    }

    public float getMin_temp() {
        return this.min_temp;
    }

    public Weather getWeather() {
        return this.weather;
    }
}
