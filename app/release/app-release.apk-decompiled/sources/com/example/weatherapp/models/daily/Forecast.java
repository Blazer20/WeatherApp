package com.example.weatherapp.models.daily;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Forecast {
    @SerializedName("data")
    private List<DataD> data;

    public List<DataD> getData() {
        return this.data;
    }
}
