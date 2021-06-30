package com.example.weatherapp.roomDB.weatherData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherData {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String dbCurrentLocation;

    public String dbCurrentDegree;

    public String dbCurrentDate;

    public String dbCurrentWeatherDescription;

    public String dbCurrentWeatherIcon;

    public String dbCurrentDegreeDay;

    public String dbCurrentDegreeNight;

    public String dbCurrentWeatherIconLow;

    public String dbCurrentWeatherDegreeLow;

    public String dbCurrentTime;

    public String dbWindValue;

    public String dbSunriseTime;

    public String dbSunsetTime;

    public int dbHumidity;

    public double dbPrecipitation;

    public double dbPressure;

    public int dbCloud;

    public double dbUv;

    public double dbVision;

    public int currentPage;

    public String cityLocation;

    public int ind;
}
