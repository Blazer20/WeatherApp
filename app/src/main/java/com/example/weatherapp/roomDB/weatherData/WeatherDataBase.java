package com.example.weatherapp.roomDB.weatherData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherData.class, HourlyData.class}, version = 1)
public abstract class WeatherDataBase extends RoomDatabase {

    private static WeatherDataBase database;

    private static String DATABASE_NAME = "weatherDataBase";

    public synchronized static WeatherDataBase getInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), WeatherDataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }

    public abstract WeatherDataDao weatherDataDao();

    public abstract HourlyDataDao hourlyDataDao();
}
