package com.example.weatherapp.roomDB.weatherData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.weatherapp.models.forecast.Hour;

import java.util.List;

@Entity
public class HourlyData {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String urlIcon;

    public double hourlyDegree;

    public String hourlyTime;

    public int ind;
}


