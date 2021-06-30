package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    private List<ForecastDay> forecastDay;

    public List<ForecastDay> getForecastDay() {
        return forecastDay;
    }
}
