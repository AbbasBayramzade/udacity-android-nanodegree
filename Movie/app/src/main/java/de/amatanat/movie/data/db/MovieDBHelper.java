package de.amatanat.movie.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amatanat.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_MOVIES_TABLE =
            "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                    MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_RATING + " TEXT NOT NULL," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_DATE + " TEXT," +
                    MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE + " TEXT)";


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
