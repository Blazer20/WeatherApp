package com.example.weatherapp.roomDB.weatherData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HourlyDataDao {

    @Query("SELECT * FROM HourlyData")
    List<HourlyData> getAllData();

    @Query("SELECT * FROM HourlyData WHERE ind = :myInd")
    List<HourlyData> getAllByIndH(int myInd);

    @Query("SELECT Id FROM HourlyData WHERE ind = :myInd")
    List<Long> getAllIdH(int myInd);

    @Query("SELECT * FROM HourlyData WHERE id = :id")
    HourlyData getByIdH(long id);

    @Query("SELECT * FROM HourlyData WHERE hourlyTime = :mHourlyTime")
    HourlyData getHourData(String mHourlyTime);

    @Insert
    long insert(HourlyData hourlyData);

    @Update
    void update(HourlyData hourlyData);

    @Delete
    void delete(HourlyData hourlyData);

    @Query("DELETE FROM HourlyData")
    void deleteTable();
}
