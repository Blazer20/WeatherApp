package com.example.weatherapp.api;

import com.example.weatherapp.models.cities.Cities;
import com.example.weatherapp.models.forecast.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("v1/forecast.json")
    Call<Data> getForecast(
            @Query("key") String apiKey,
            @Query("days") int days,
            @Query("q") String q,
            @Query("lang") String lang
    );

    @GET("v1/search.json")
    Call<List<Cities>> getCities(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("lang") String lang
    );
}
