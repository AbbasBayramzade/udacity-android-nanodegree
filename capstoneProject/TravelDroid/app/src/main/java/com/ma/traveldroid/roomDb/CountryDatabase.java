package com.ma.traveldroid.roomDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {CountryEntry.class}, version = 1, exportSchema = false)
public abstract class CountryDatabase extends RoomDatabase {

    private static final String TAG = CountryDatabase.class.getName();

    private static final String DATABASE_NAME = "country";
    private static volatile CountryDatabase sInstance;
    private static final Object LOCK = new Object();

    // Create singleton CountryDatabase
    public static CountryDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    Log.i(TAG, "Creating a new database");
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CountryDatabase.class, CountryDatabase.DATABASE_NAME)
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract CountryDao countryDao();

}
