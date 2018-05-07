package com.ma.traveldroid.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CountryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "countries.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_COUNTRIES_TABLE =
            "CREATE TABLE " + CountryContract.CountryEntry.TABLE_NAME_COUNTRIES + " (" +
                    CountryContract.CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CountryContract.CountryEntry.COLUMN_COUNTRY_NAME + " TEXT NOT NULL," +
                    CountryContract.CountryEntry.COLUMN_VISITED_PERIOD + " TEXT)";
              //      CountryContract.CountryEntry.COLUMN_MAP_CONTEXT + " TEXT)";

//    private static final String SQL_CREATE_MAP_CONTENT_TABLE =
//            "CREATE TABLE " +  CountryContract.CountryEntry.TABLE_NAME_MAP_CONTENT + " (" +
//                    CountryContract.CountryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    CountryContract.CountryEntry.COLUMN_MAP_CONTENT_TITLE + " TEXT NOT NULL, " +
//                    " FOREIGN KEY (" +  CountryContract.CountryEntry.COLUMN_MAP_CONTENT_TITLE + ") REFERENCES " +
//                    CountryContract.CountryEntry.TABLE_NAME_COUNTRIES + "(" + CountryContract.CountryEntry.COLUMN_COUNTRY_NAME + "));";

    public CountryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COUNTRIES_TABLE);
      //  db.execSQL(SQL_CREATE_MAP_CONTENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // ignore update
    }
}
