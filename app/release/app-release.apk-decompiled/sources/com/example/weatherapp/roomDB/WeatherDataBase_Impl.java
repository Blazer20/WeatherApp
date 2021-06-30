package com.example.weatherapp.roomDB;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenHelper;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.util.HashMap;
import java.util.HashSet;

public final class WeatherDataBase_Impl extends WeatherDataBase {
    private volatile WeatherDataDao _weatherDataDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(SupportSQLiteOpenHelper.Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new RoomOpenHelper.Delegate(1) {
            public void onPostMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
            }

            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `WeatherData` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dbCurrentLocation` TEXT, `dbCurrentDegree` TEXT, `dbCurrentWeatherIcon` TEXT, `dbHourlyTime` TEXT, `dbHourlyIcon` TEXT, `dbHourlyDegree` TEXT, `dbDailyWeekday` TEXT, `dbDailyDayIcon` TEXT, `dbDailyNightIcon` TEXT, `dbDailyMaxDegree` TEXT, `dbDailyMinDegree` TEXT, `ind` INTEGER NOT NULL)");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7a6922140a84377c7e5cf3909abb3cc4')");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `WeatherData`");
                if (WeatherDataBase_Impl.this.mCallbacks != null) {
                    int size = WeatherDataBase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WeatherDataBase_Impl.this.mCallbacks.get(i)).onDestructiveMigration(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (WeatherDataBase_Impl.this.mCallbacks != null) {
                    int size = WeatherDataBase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WeatherDataBase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SupportSQLiteDatabase unused = WeatherDataBase_Impl.this.mDatabase = supportSQLiteDatabase;
                WeatherDataBase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (WeatherDataBase_Impl.this.mCallbacks != null) {
                    int size = WeatherDataBase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((RoomDatabase.Callback) WeatherDataBase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            public void onPreMigrate(SupportSQLiteDatabase supportSQLiteDatabase) {
                DBUtil.dropFtsSyncTriggers(supportSQLiteDatabase);
            }

            /* access modifiers changed from: protected */
            public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(13);
                hashMap.put("id", new TableInfo.Column("id", "INTEGER", true, 1, (String) null, 1));
                hashMap.put("dbCurrentLocation", new TableInfo.Column("dbCurrentLocation", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbCurrentDegree", new TableInfo.Column("dbCurrentDegree", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbCurrentWeatherIcon", new TableInfo.Column("dbCurrentWeatherIcon", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbHourlyTime", new TableInfo.Column("dbHourlyTime", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbHourlyIcon", new TableInfo.Column("dbHourlyIcon", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbHourlyDegree", new TableInfo.Column("dbHourlyDegree", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbDailyWeekday", new TableInfo.Column("dbDailyWeekday", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbDailyDayIcon", new TableInfo.Column("dbDailyDayIcon", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbDailyNightIcon", new TableInfo.Column("dbDailyNightIcon", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbDailyMaxDegree", new TableInfo.Column("dbDailyMaxDegree", "TEXT", false, 0, (String) null, 1));
                hashMap.put("dbDailyMinDegree", new TableInfo.Column("dbDailyMinDegree", "TEXT", false, 0, (String) null, 1));
                hashMap.put("ind", new TableInfo.Column("ind", "INTEGER", true, 0, (String) null, 1));
                TableInfo tableInfo = new TableInfo("WeatherData", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "WeatherData");
                if (tableInfo.equals(read)) {
                    return new RoomOpenHelper.ValidationResult(true, (String) null);
                }
                return new RoomOpenHelper.ValidationResult(false, "WeatherData(com.example.weatherapp.roomDB.WeatherData).\n Expected:\n" + tableInfo + "\n Found:\n" + read);
            }
        }, "7a6922140a84377c7e5cf3909abb3cc4", "f962602fcb14e3440d4428699bd58889")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, new HashMap(0), new HashMap(0), "WeatherData");
    }

    public void clearAllTables() {
        super.assertNotMainThread();
        SupportSQLiteDatabase writableDatabase = super.getOpenHelper().getWritableDatabase();
        try {
            super.beginTransaction();
            writableDatabase.execSQL("DELETE FROM `WeatherData`");
            super.setTransactionSuccessful();
        } finally {
            super.endTransaction();
            writableDatabase.query("PRAGMA wal_checkpoint(FULL)").close();
            if (!writableDatabase.inTransaction()) {
                writableDatabase.execSQL("VACUUM");
            }
        }
    }

    public WeatherDataDao weatherDataDao() {
        WeatherDataDao weatherDataDao;
        if (this._weatherDataDao != null) {
            return this._weatherDataDao;
        }
        synchronized (this) {
            if (this._weatherDataDao == null) {
                this._weatherDataDao = new WeatherDataDao_Impl(this);
            }
            weatherDataDao = this._weatherDataDao;
        }
        return weatherDataDao;
    }
}
