package de.amatanat.movie.data.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by amatanat.
 */

public class MovieProvider extends ContentProvider {

    private static final String LOG_TAG = MovieProvider.class.getName();

    // URI matcher code for the content uri for the table "movies"
    private static final int MOVIES = 1;

    //URI matcher code for the content uri for the specific row
    private static final int MOVIES_ID = 2;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        mUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_ID);
    }

    private MovieDBHelper movieDBHelper;

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        Cursor cursor;

        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                // query the whole table
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case MOVIES_ID:
                // query specific id indicated in the uri
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_LIST_TYPE;
            case MOVIES_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Illegal URI in gettype " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        long id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Error in inserting a new row");
        } else {
            Log.i(LOG_TAG, "Row is inserted");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedRows;
        // get database
        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                // delete all rows from the table
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (deletedRows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deletedRows;
            case MOVIES_ID:
                // delete specific rows from tha table according to specified conditions
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (deletedRows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deletedRows;
            default:
                throw new IllegalArgumentException("Illegal uri in delete operation");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return updateProducts(uri, contentValues, selection, selectionArgs);
            case MOVIES_ID:
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProducts(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot update unknown URI " + uri);
        }
    }

    private int updateProducts(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues == null){
            throw new IllegalArgumentException("Content values cannot be null");
        }

        SQLiteDatabase db = movieDBHelper.getWritableDatabase();

        // update db and return id of the updated row
        int updatedRow = db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (updatedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updatedRow;
    }
}
