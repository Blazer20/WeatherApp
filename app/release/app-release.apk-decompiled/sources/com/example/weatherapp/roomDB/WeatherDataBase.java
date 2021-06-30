package com.example.weatherapp.roomDB;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class WeatherDataBase extends RoomDatabase {
    private static String DATABASE_NAME = "weatherDataBase";
    private static WeatherDataBase database;

    public abstract WeatherDataDao weatherDataDao();

    public static synchronized WeatherDataBase getInstance(Context context) {
        WeatherDataBase weatherDataBase;
        Class<WeatherDataBase> cls = WeatherDataBase.class;
        synchronized (cls) {
            if (database == null) {
                database = Room.databaseBuilder(context.getApplicationContext(), cls, DATABASE_NAME).allowMainThreadQueries().build();
            }
            weatherDataBase = database;
        }
        return weatherDataBase;
    }
}
