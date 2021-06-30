package com.example.weatherapp.roomDB.cityNameList;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CityName.class}, version = 2)
public abstract class CityNameBase extends RoomDatabase {

    private static CityNameBase cityDataBase;

    private static String DATABASE_NAME = "citiesDataBase";

   public synchronized static CityNameBase getInstance(Context context) {
       if (cityDataBase == null) {
           cityDataBase = Room.databaseBuilder(context.getApplicationContext(), CityNameBase.class, DATABASE_NAME)
                   .createFromAsset("cities.db")
                   .fallbackToDestructiveMigration()
                   .allowMainThreadQueries()
                   .build();
       }
       return cityDataBase;
   }

   public abstract CityNameDao cityNameDao();
}
