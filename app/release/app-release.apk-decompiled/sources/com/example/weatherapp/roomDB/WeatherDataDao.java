package com.example.weatherapp.roomDB;

import java.util.List;

public interface WeatherDataDao {
    void delete(WeatherData weatherData);

    void deleteTable();

    List<WeatherData> getAllByIndD();

    List<WeatherData> getAllByIndH();

    List<WeatherData> getAllData();

    List<Long> getAllIdD();

    List<Long> getAllIdH();

    WeatherData getById(long j);

    int getCountD();

    int getCountH();

    long insert(WeatherData weatherData);

    void update(WeatherData weatherData);

    void updateD(String str, String str2, String str3, String str4, String str5, int i);

    void updateH(String str, String str2, String str3, int i);
}
