package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ForecastDay {

    @SerializedName("date")
    private Date date;

    @SerializedName("day")
    private Day day;

    @SerializedName("hour")
    private List<Hour> hour;

    @SerializedName("astro")
    private Astro astro;

    public Date getDate() {
        return date;
    }

    public Day getDay() {
        return day;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public Astro getAstro() {
        return astro;
    }
}
