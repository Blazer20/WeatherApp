package com.example.weatherapp.roomDB.cityNameList;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "city")
public class CityName {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
}
