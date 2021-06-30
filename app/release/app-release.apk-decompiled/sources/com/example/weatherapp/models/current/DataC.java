package com.example.weatherapp.models.current;

import com.example.weatherapp.models.hourly.Weather;
import com.google.gson.annotations.SerializedName;

public class DataC {
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("temp")
    private float temp;
    @SerializedName("weather")
    private Weather weather;

    public float getTemp() {
        return this.temp;
    }

    public Weather getWeather() {
        return this.weather;
    }

    public String getCityName() {
        return this.cityName;
    }
}
