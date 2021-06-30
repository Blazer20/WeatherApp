package com.example.weatherapp.roomDB.weatherData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDataDao {

    @Query("SELECT * FROM WeatherData")
    List<WeatherData> getAllData();

    @Query("SELECT * FROM WeatherData WHERE ind = :myInd")
    List<WeatherData> getAllByInd(int myInd);

    @Query("SELECT ind FROM WeatherData WHERE cityLocation = :q")
    int getCurrentPage(String q);

    @Query("SELECT cityLocation FROM WeatherData WHERE cityLocation = :q")
    boolean findExistence(String q);

    @Query("SELECT cityLocation FROM WeatherData WHERE ind = :myCurrentPager")
    String getCurrentLocation(int myCurrentPager);

    @Query("SELECT * FROM WeatherData WHERE cityLocation = :q")
    List<WeatherData> getCity(String q);

    @Insert
    long insert(WeatherData weatherData);

    @Update
    void update(WeatherData weatherData);

    @Delete
    void delete(WeatherData weatherData);

    @Query("DELETE FROM WeatherData")
    void deleteTable();
}
