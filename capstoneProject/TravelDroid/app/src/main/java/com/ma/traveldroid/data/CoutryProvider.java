package com.ma.traveldroid.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CoutryProvider extends ContentProvider {

    private static final String LOG_TAG = CoutryProvider.class.getName();

    // URI matcher code for the content uri for the table "countries"
    private static final int COUNTRIES = 1;

    // URI matcher code for the content uri for the specific row for the table "songs"
    private static final int PATH_COUNTRIES_COUNTRY_NAME = 4;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(CountryContract.CONTENT_AUTHORITY, CountryContract.PATH_COUNTRIES, COUNTRIES);
        mUriMatcher.addURI(CountryContract.CONTENT_AUTHORITY, CountryContract.PATH_COUNTRIES + "/#", PATH_COUNTRIES_COUNTRY_NAME);
    }

    private CountryDbHelper mCoutryDbHelper;

    @Override
    public boolean onCreate() {
        mCoutryDbHelper = new CountryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,  String[] projection, String selection,
                        String[] selectionArgs,  String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db = mCoutryDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                // query the whole "countries" table
                cursor = db.query(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                return CountryContract.CountryEntry.CONTENT_LIST_TYPE;
            case PATH_COUNTRIES_COUNTRY_NAME:
                return CountryContract.CountryEntry.CONTENT_COUNTRY_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Illegal URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
