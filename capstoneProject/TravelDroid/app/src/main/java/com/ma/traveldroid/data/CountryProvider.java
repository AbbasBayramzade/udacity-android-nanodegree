package com.ma.traveldroid.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

public class CountryProvider extends ContentProvider {

    private static final String LOG_TAG = CountryProvider.class.getName();

    // URI matcher code for the content uri for the table "countries"
    private static final int COUNTRIES = 1;

    // URI matcher code for the content uri for the specific row for the table "countries"
    private static final int COUNTRIES_ID = 2;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(CountryContract.CONTENT_AUTHORITY, CountryContract.PATH_COUNTRIES, COUNTRIES);
        mUriMatcher.addURI(CountryContract.CONTENT_AUTHORITY, CountryContract.PATH_COUNTRIES + "/#", COUNTRIES_ID);
    }

    private CountryDbHelper mCountryDbHelper;

    @Override
    public boolean onCreate() {
        mCountryDbHelper = new CountryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri,  String[] projection, String selection,
                        String[] selectionArgs,  String sortOrder) {

        Cursor cursor;
        SQLiteDatabase db = mCountryDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                // query the whole "countries" table
                cursor = db.query(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COUNTRIES_ID:
                // query specific id indicated in the uri
                selection = CountryContract.CountryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
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
            case COUNTRIES_ID:
                return CountryContract.CountryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Illegal URI" + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                return insertCountry(uri, values);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private Uri insertCountry(Uri uri, ContentValues values) {
        sanityCheck(values);

        SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();
        long id = db.insert(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Error in inserting a new row");
        } else {
            Log.i(LOG_TAG, "Row is inserted");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
    * Check that user has entered country name
    * Visited period can be empty
     */
    private void sanityCheck(ContentValues values) {
        // check whether country name is empty or not
        String countryName = values.getAsString(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME);
        if (TextUtils.isEmpty(countryName)) {
            throw new IllegalArgumentException("Country name cannot be empty");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedRows;
        SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                // delete all rows from the table
                deletedRows = db.delete(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, selection, selectionArgs);
                if (deletedRows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deletedRows;
            case COUNTRIES_ID:
                // delete specific rows from tha table according to specified conditions
                selection = CountryContract.CountryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, selection, selectionArgs);
                if (deletedRows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deletedRows;
            default:
                throw new IllegalArgumentException("Illegal uri");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case COUNTRIES:
                return updateCountries(uri, values, selection, selectionArgs);
            case COUNTRIES_ID:
                selection = CountryContract.CountryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCountries(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private int updateCountries(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

         sanityCheck(values);

        // check if contentvalues size is 0 or not
        if (values.size() == 0) {
            return 0;
        }

        // get writeable databse
        SQLiteDatabase db = mCountryDbHelper.getWritableDatabase();

        // update db and return id of the updated row
        int updatedRow = db.update(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, values, selection, selectionArgs);

        if (updatedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRow;
    }
}
