package com.example.weatherapp.models.cities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Predictions {

    @SerializedName("terms")
    private List<Terms> termsList;

    public List<Terms> getTermsList() {
        return termsList;
    }
}
