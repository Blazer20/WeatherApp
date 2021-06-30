package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Current {

    @SerializedName("temp_c")
    private double temp;

    @SerializedName("condition")
    private Condition condition;

    @SerializedName("wind_kph")
    private double wind;

    @SerializedName("pressure_mb")
    private double pressure;

    @SerializedName("precip_mm")
    private double precipitation ;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("cloud")
    private int cloud;

    @SerializedName("uv")
    private double uv;

    @SerializedName("vis_km")
    private double vision;

    public double getTemp() {
        return temp;
    }

    public Condition getCondition() {
        return condition;
    }

    public double getWind() {
        return wind;
    }

    public double getPressure() {
        return pressure;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getCloud() {
        return cloud;
    }

    public double getUv() {
        return uv;
    }

    public double getVision() {
        return vision;
    }

}
