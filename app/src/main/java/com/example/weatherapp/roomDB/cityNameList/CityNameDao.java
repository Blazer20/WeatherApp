package com.example.weatherapp.roomDB.cityNameList;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CityNameDao {
    @Query("SELECT * FROM city")
    List<CityName> getCities();

    @Insert
    void insert1(CityName cityName);

    @Query("DELETE FROM city")
    void deleteTable1();


}
