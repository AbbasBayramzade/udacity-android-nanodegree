package de.amatanat.popularmovies.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by amatanat.
 */
@Database(entities = {MovieEntry.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase{

    private static final String DATABASE_NAME = "movies";

    // getter for DAO
    public abstract MovieDao movieDao();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile MovieDatabase sInstance;

    /**
     * Get the singleton for this class
     */
    public static MovieDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            MovieDatabase.class, MovieDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }
}
