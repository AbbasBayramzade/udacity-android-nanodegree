package com.ma.bakingrecipes.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.ma.bakingrecipes.model.Recipe;

/**
 * Created by amatanat.
 */

@Database(entities = Recipe.class, version = 1)
@TypeConverters(Convertors.class)
public abstract class BakingDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipe";
    private static volatile BakingDatabase sInstance;
    private static final Object LOCK = new Object();

    // Create singleton BakingDatabase
    public static BakingDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            BakingDatabase.class, BakingDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
}
