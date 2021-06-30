package com.example.weatherapp.api;

import com.example.weatherapp.models.current.Current;
import com.example.weatherapp.models.daily.Forecast;
import com.example.weatherapp.models.hourly.Hourly;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2.0/current")
    Call<Current> getCurrent(@Query("key") String str, @Query("city") String str2);

    @GET("v2.0/forecast/daily")
    Call<Forecast> getForecast(@Query("days") int i, @Query("city") String str, @Query("key") String str2);

    @GET("v2.0/forecast/hourly")
    Call<Hourly> getHourly(@Query("key") String str, @Query("city") String str2, @Query("hours") int i);
}
