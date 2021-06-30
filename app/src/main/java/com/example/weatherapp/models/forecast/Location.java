package com.example.weatherapp.models.forecast;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    private String cityName;

//    @SerializedName("region")
//    private String region;
//
//    @SerializedName("country")
//    private String country;
//
//    @SerializedName("lat")
//    private String lat;
//
//    @SerializedName("lon")
//    private String lon;
//
//    @SerializedName("tz_id")
//    private String tz;
//
//    @SerializedName("localtime_epoch")
//    private String epoch;

    @SerializedName("localtime")
    private String time;

    public String getCityName() {
        return cityName;
    }

//    public String getRegion() {
//        return region;
//    }
//
//    public String getCountry() {
//        return country;
//    }
//
//    public String getLat() {
//        return lat;
//    }
//
//    public String getLon() {
//        return lon;
//    }
//
//    public String getTz() {
//        return tz;
//    }
//
//    public String getEpoch() {
//        return epoch;
//    }

    public String getTime() {
        return cityName;
    }
}
