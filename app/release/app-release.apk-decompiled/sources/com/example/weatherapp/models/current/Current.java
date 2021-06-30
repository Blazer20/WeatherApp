package com.example.weatherapp.models.current;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Current {
    @SerializedName("data")
    private List<DataC> data;

    public List<DataC> getData() {
        return this.data;
    }
}
