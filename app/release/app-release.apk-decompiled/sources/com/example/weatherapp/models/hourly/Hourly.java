package com.example.weatherapp.models.hourly;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Hourly {
    @SerializedName("data")
    private List<DataH> data;

    public List<DataH> getData() {
        return this.data;
    }
}
