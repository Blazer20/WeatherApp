package com.example.weatherapp.roomDB.weatherData;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dailyTable")
public class DailyData {

    @PrimaryKey(autoGenerate = true)
    public long id;

}
