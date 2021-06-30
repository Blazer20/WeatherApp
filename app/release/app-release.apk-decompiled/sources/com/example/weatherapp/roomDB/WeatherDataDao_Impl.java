package com.example.weatherapp.roomDB;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.util.ArrayList;
import java.util.List;

public final class WeatherDataDao_Impl implements WeatherDataDao {
    private final RoomDatabase __db;
    private final EntityDeletionOrUpdateAdapter<WeatherData> __deletionAdapterOfWeatherData;
    private final EntityInsertionAdapter<WeatherData> __insertionAdapterOfWeatherData;
    private final SharedSQLiteStatement __preparedStmtOfDeleteTable;
    private final SharedSQLiteStatement __preparedStmtOfUpdateD;
    private final SharedSQLiteStatement __preparedStmtOfUpdateH;
    private final EntityDeletionOrUpdateAdapter<WeatherData> __updateAdapterOfWeatherData;

    public WeatherDataDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfWeatherData = new EntityInsertionAdapter<WeatherData>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `WeatherData` (`id`,`dbCurrentLocation`,`dbCurrentDegree`,`dbCurrentWeatherIcon`,`dbHourlyTime`,`dbHourlyIcon`,`dbHourlyDegree`,`dbDailyWeekday`,`dbDailyDayIcon`,`dbDailyNightIcon`,`dbDailyMaxDegree`,`dbDailyMinDegree`,`ind`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WeatherData weatherData) {
                supportSQLiteStatement.bindLong(1, weatherData.id);
                if (weatherData.dbCurrentLocation == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, weatherData.dbCurrentLocation);
                }
                if (weatherData.dbCurrentDegree == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, weatherData.dbCurrentDegree);
                }
                if (weatherData.dbCurrentWeatherIcon == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, weatherData.dbCurrentWeatherIcon);
                }
                if (weatherData.dbHourlyTime == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, weatherData.dbHourlyTime);
                }
                if (weatherData.dbHourlyIcon == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, weatherData.dbHourlyIcon);
                }
                if (weatherData.dbHourlyDegree == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, weatherData.dbHourlyDegree);
                }
                if (weatherData.dbDailyWeekday == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, weatherData.dbDailyWeekday);
                }
                if (weatherData.dbDailyDayIcon == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindString(9, weatherData.dbDailyDayIcon);
                }
                if (weatherData.dbDailyNightIcon == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindString(10, weatherData.dbDailyNightIcon);
                }
                if (weatherData.dbDailyMaxDegree == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindString(11, weatherData.dbDailyMaxDegree);
                }
                if (weatherData.dbDailyMinDegree == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindString(12, weatherData.dbDailyMinDegree);
                }
                supportSQLiteStatement.bindLong(13, (long) weatherData.ind);
            }
        };
        this.__deletionAdapterOfWeatherData = new EntityDeletionOrUpdateAdapter<WeatherData>(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM `WeatherData` WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WeatherData weatherData) {
                supportSQLiteStatement.bindLong(1, weatherData.id);
            }
        };
        this.__updateAdapterOfWeatherData = new EntityDeletionOrUpdateAdapter<WeatherData>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR ABORT `WeatherData` SET `id` = ?,`dbCurrentLocation` = ?,`dbCurrentDegree` = ?,`dbCurrentWeatherIcon` = ?,`dbHourlyTime` = ?,`dbHourlyIcon` = ?,`dbHourlyDegree` = ?,`dbDailyWeekday` = ?,`dbDailyDayIcon` = ?,`dbDailyNightIcon` = ?,`dbDailyMaxDegree` = ?,`dbDailyMinDegree` = ?,`ind` = ? WHERE `id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, WeatherData weatherData) {
                supportSQLiteStatement.bindLong(1, weatherData.id);
                if (weatherData.dbCurrentLocation == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, weatherData.dbCurrentLocation);
                }
                if (weatherData.dbCurrentDegree == null) {
                    supportSQLiteStatement.bindNull(3);
                } else {
                    supportSQLiteStatement.bindString(3, weatherData.dbCurrentDegree);
                }
                if (weatherData.dbCurrentWeatherIcon == null) {
                    supportSQLiteStatement.bindNull(4);
                } else {
                    supportSQLiteStatement.bindString(4, weatherData.dbCurrentWeatherIcon);
                }
                if (weatherData.dbHourlyTime == null) {
                    supportSQLiteStatement.bindNull(5);
                } else {
                    supportSQLiteStatement.bindString(5, weatherData.dbHourlyTime);
                }
                if (weatherData.dbHourlyIcon == null) {
                    supportSQLiteStatement.bindNull(6);
                } else {
                    supportSQLiteStatement.bindString(6, weatherData.dbHourlyIcon);
                }
                if (weatherData.dbHourlyDegree == null) {
                    supportSQLiteStatement.bindNull(7);
                } else {
                    supportSQLiteStatement.bindString(7, weatherData.dbHourlyDegree);
                }
                if (weatherData.dbDailyWeekday == null) {
                    supportSQLiteStatement.bindNull(8);
                } else {
                    supportSQLiteStatement.bindString(8, weatherData.dbDailyWeekday);
                }
                if (weatherData.dbDailyDayIcon == null) {
                    supportSQLiteStatement.bindNull(9);
                } else {
                    supportSQLiteStatement.bindString(9, weatherData.dbDailyDayIcon);
                }
                if (weatherData.dbDailyNightIcon == null) {
                    supportSQLiteStatement.bindNull(10);
                } else {
                    supportSQLiteStatement.bindString(10, weatherData.dbDailyNightIcon);
                }
                if (weatherData.dbDailyMaxDegree == null) {
                    supportSQLiteStatement.bindNull(11);
                } else {
                    supportSQLiteStatement.bindString(11, weatherData.dbDailyMaxDegree);
                }
                if (weatherData.dbDailyMinDegree == null) {
                    supportSQLiteStatement.bindNull(12);
                } else {
                    supportSQLiteStatement.bindString(12, weatherData.dbDailyMinDegree);
                }
                supportSQLiteStatement.bindLong(13, (long) weatherData.ind);
                supportSQLiteStatement.bindLong(14, weatherData.id);
            }
        };
        this.__preparedStmtOfUpdateH = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "UPDATE weatherdata SET dbHourlyTime=?, dbHourlyIcon=?, dbHourlyDegree=? WHERE id=? and ind=1";
            }
        };
        this.__preparedStmtOfUpdateD = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "UPDATE weatherdata SET dbDailyWeekday=?, dbDailyDayIcon=?, dbDailyNightIcon=?, dbDailyMaxDegree=?, dbDailyMinDegree=? WHERE id=? and ind=2";
            }
        };
        this.__preparedStmtOfDeleteTable = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "DELETE FROM weatherdata";
            }
        };
    }

    public long insert(WeatherData weatherData) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            long insertAndReturnId = this.__insertionAdapterOfWeatherData.insertAndReturnId(weatherData);
            this.__db.setTransactionSuccessful();
            return insertAndReturnId;
        } finally {
            this.__db.endTransaction();
        }
    }

    public void delete(WeatherData weatherData) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__deletionAdapterOfWeatherData.handle(weatherData);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(WeatherData weatherData) {
        this.__db.assertNotSuspendingTransaction();
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfWeatherData.handle(weatherData);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void updateH(String str, String str2, String str3, int i) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateH.acquire();
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        if (str2 == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str2);
        }
        if (str3 == null) {
            acquire.bindNull(3);
        } else {
            acquire.bindString(3, str3);
        }
        acquire.bindLong(4, (long) i);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateH.release(acquire);
        }
    }

    public void updateD(String str, String str2, String str3, String str4, String str5, int i) {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfUpdateD.acquire();
        if (str == null) {
            acquire.bindNull(1);
        } else {
            acquire.bindString(1, str);
        }
        if (str2 == null) {
            acquire.bindNull(2);
        } else {
            acquire.bindString(2, str2);
        }
        if (str3 == null) {
            acquire.bindNull(3);
        } else {
            acquire.bindString(3, str3);
        }
        if (str4 == null) {
            acquire.bindNull(4);
        } else {
            acquire.bindString(4, str4);
        }
        if (str5 == null) {
            acquire.bindNull(5);
        } else {
            acquire.bindString(5, str5);
        }
        acquire.bindLong(6, (long) i);
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfUpdateD.release(acquire);
        }
    }

    public void deleteTable() {
        this.__db.assertNotSuspendingTransaction();
        SupportSQLiteStatement acquire = this.__preparedStmtOfDeleteTable.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfDeleteTable.release(acquire);
        }
    }

    public List<WeatherData> getAllData() {
        RoomSQLiteQuery roomSQLiteQuery;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM weatherdata", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentLocation");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentDegree");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentWeatherIcon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyIcon");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyDegree");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyWeekday");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyDayIcon");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyNightIcon");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMaxDegree");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMinDegree");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "ind");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    WeatherData weatherData = new WeatherData();
                    weatherData.id = query.getLong(columnIndexOrThrow);
                    weatherData.dbCurrentLocation = query.getString(columnIndexOrThrow2);
                    weatherData.dbCurrentDegree = query.getString(columnIndexOrThrow3);
                    weatherData.dbCurrentWeatherIcon = query.getString(columnIndexOrThrow4);
                    weatherData.dbHourlyTime = query.getString(columnIndexOrThrow5);
                    weatherData.dbHourlyIcon = query.getString(columnIndexOrThrow6);
                    weatherData.dbHourlyDegree = query.getString(columnIndexOrThrow7);
                    weatherData.dbDailyWeekday = query.getString(columnIndexOrThrow8);
                    weatherData.dbDailyDayIcon = query.getString(columnIndexOrThrow9);
                    weatherData.dbDailyNightIcon = query.getString(columnIndexOrThrow10);
                    weatherData.dbDailyMaxDegree = query.getString(columnIndexOrThrow11);
                    columnIndexOrThrow12 = columnIndexOrThrow12;
                    weatherData.dbDailyMinDegree = query.getString(columnIndexOrThrow12);
                    int i = columnIndexOrThrow;
                    columnIndexOrThrow13 = columnIndexOrThrow13;
                    weatherData.ind = query.getInt(columnIndexOrThrow13);
                    arrayList.add(weatherData);
                    columnIndexOrThrow = i;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<WeatherData> getAllByIndH() {
        RoomSQLiteQuery roomSQLiteQuery;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM weatherdata WHERE ind=1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentLocation");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentDegree");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentWeatherIcon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyIcon");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyDegree");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyWeekday");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyDayIcon");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyNightIcon");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMaxDegree");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMinDegree");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "ind");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    WeatherData weatherData = new WeatherData();
                    weatherData.id = query.getLong(columnIndexOrThrow);
                    weatherData.dbCurrentLocation = query.getString(columnIndexOrThrow2);
                    weatherData.dbCurrentDegree = query.getString(columnIndexOrThrow3);
                    weatherData.dbCurrentWeatherIcon = query.getString(columnIndexOrThrow4);
                    weatherData.dbHourlyTime = query.getString(columnIndexOrThrow5);
                    weatherData.dbHourlyIcon = query.getString(columnIndexOrThrow6);
                    weatherData.dbHourlyDegree = query.getString(columnIndexOrThrow7);
                    weatherData.dbDailyWeekday = query.getString(columnIndexOrThrow8);
                    weatherData.dbDailyDayIcon = query.getString(columnIndexOrThrow9);
                    weatherData.dbDailyNightIcon = query.getString(columnIndexOrThrow10);
                    weatherData.dbDailyMaxDegree = query.getString(columnIndexOrThrow11);
                    columnIndexOrThrow12 = columnIndexOrThrow12;
                    weatherData.dbDailyMinDegree = query.getString(columnIndexOrThrow12);
                    int i = columnIndexOrThrow;
                    columnIndexOrThrow13 = columnIndexOrThrow13;
                    weatherData.ind = query.getInt(columnIndexOrThrow13);
                    arrayList.add(weatherData);
                    columnIndexOrThrow = i;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<WeatherData> getAllByIndD() {
        RoomSQLiteQuery roomSQLiteQuery;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM weatherdata WHERE ind=2", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentLocation");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentDegree");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentWeatherIcon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyIcon");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyDegree");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyWeekday");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyDayIcon");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyNightIcon");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMaxDegree");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMinDegree");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "ind");
            roomSQLiteQuery = acquire;
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    WeatherData weatherData = new WeatherData();
                    weatherData.id = query.getLong(columnIndexOrThrow);
                    weatherData.dbCurrentLocation = query.getString(columnIndexOrThrow2);
                    weatherData.dbCurrentDegree = query.getString(columnIndexOrThrow3);
                    weatherData.dbCurrentWeatherIcon = query.getString(columnIndexOrThrow4);
                    weatherData.dbHourlyTime = query.getString(columnIndexOrThrow5);
                    weatherData.dbHourlyIcon = query.getString(columnIndexOrThrow6);
                    weatherData.dbHourlyDegree = query.getString(columnIndexOrThrow7);
                    weatherData.dbDailyWeekday = query.getString(columnIndexOrThrow8);
                    weatherData.dbDailyDayIcon = query.getString(columnIndexOrThrow9);
                    weatherData.dbDailyNightIcon = query.getString(columnIndexOrThrow10);
                    weatherData.dbDailyMaxDegree = query.getString(columnIndexOrThrow11);
                    columnIndexOrThrow12 = columnIndexOrThrow12;
                    weatherData.dbDailyMinDegree = query.getString(columnIndexOrThrow12);
                    int i = columnIndexOrThrow;
                    columnIndexOrThrow13 = columnIndexOrThrow13;
                    weatherData.ind = query.getInt(columnIndexOrThrow13);
                    arrayList.add(weatherData);
                    columnIndexOrThrow = i;
                }
                query.close();
                roomSQLiteQuery.release();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                query.close();
                roomSQLiteQuery.release();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public WeatherData getById(long j) {
        RoomSQLiteQuery roomSQLiteQuery;
        WeatherData weatherData;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM weatherdata WHERE id = ?", 1);
        acquire.bindLong(1, j);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            int columnIndexOrThrow = CursorUtil.getColumnIndexOrThrow(query, "id");
            int columnIndexOrThrow2 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentLocation");
            int columnIndexOrThrow3 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentDegree");
            int columnIndexOrThrow4 = CursorUtil.getColumnIndexOrThrow(query, "dbCurrentWeatherIcon");
            int columnIndexOrThrow5 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyTime");
            int columnIndexOrThrow6 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyIcon");
            int columnIndexOrThrow7 = CursorUtil.getColumnIndexOrThrow(query, "dbHourlyDegree");
            int columnIndexOrThrow8 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyWeekday");
            int columnIndexOrThrow9 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyDayIcon");
            int columnIndexOrThrow10 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyNightIcon");
            int columnIndexOrThrow11 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMaxDegree");
            int columnIndexOrThrow12 = CursorUtil.getColumnIndexOrThrow(query, "dbDailyMinDegree");
            int columnIndexOrThrow13 = CursorUtil.getColumnIndexOrThrow(query, "ind");
            if (query.moveToFirst()) {
                WeatherData weatherData2 = new WeatherData();
                roomSQLiteQuery = acquire;
                int i = columnIndexOrThrow13;
                try {
                    weatherData2.id = query.getLong(columnIndexOrThrow);
                    weatherData2.dbCurrentLocation = query.getString(columnIndexOrThrow2);
                    weatherData2.dbCurrentDegree = query.getString(columnIndexOrThrow3);
                    weatherData2.dbCurrentWeatherIcon = query.getString(columnIndexOrThrow4);
                    weatherData2.dbHourlyTime = query.getString(columnIndexOrThrow5);
                    weatherData2.dbHourlyIcon = query.getString(columnIndexOrThrow6);
                    weatherData2.dbHourlyDegree = query.getString(columnIndexOrThrow7);
                    weatherData2.dbDailyWeekday = query.getString(columnIndexOrThrow8);
                    weatherData2.dbDailyDayIcon = query.getString(columnIndexOrThrow9);
                    weatherData2.dbDailyNightIcon = query.getString(columnIndexOrThrow10);
                    weatherData2.dbDailyMaxDegree = query.getString(columnIndexOrThrow11);
                    weatherData2.dbDailyMinDegree = query.getString(columnIndexOrThrow12);
                    weatherData2.ind = query.getInt(i);
                    weatherData = weatherData2;
                } catch (Throwable th) {
                    th = th;
                    query.close();
                    roomSQLiteQuery.release();
                    throw th;
                }
            } else {
                roomSQLiteQuery = acquire;
                weatherData = null;
            }
            query.close();
            roomSQLiteQuery.release();
            return weatherData;
        } catch (Throwable th2) {
            th = th2;
            roomSQLiteQuery = acquire;
            query.close();
            roomSQLiteQuery.release();
            throw th;
        }
    }

    public List<Long> getAllIdH() {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT Id FROM weatherdata WHERE ind=1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                if (query.isNull(0)) {
                    l = null;
                } else {
                    l = Long.valueOf(query.getLong(0));
                }
                arrayList.add(l);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<Long> getAllIdD() {
        Long l;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT Id FROM weatherdata WHERE ind=2", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                if (query.isNull(0)) {
                    l = null;
                } else {
                    l = Long.valueOf(query.getLong(0));
                }
                arrayList.add(l);
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getCountH() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM weatherdata WHERE ind=1", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                i = query.getInt(0);
            }
            return i;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public int getCountD() {
        int i = 0;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT COUNT(*) FROM weatherdata WHERE ind=2", 0);
        this.__db.assertNotSuspendingTransaction();
        Cursor query = DBUtil.query(this.__db, acquire, false, (CancellationSignal) null);
        try {
            if (query.moveToFirst()) {
                i = query.getInt(0);
            }
            return i;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
