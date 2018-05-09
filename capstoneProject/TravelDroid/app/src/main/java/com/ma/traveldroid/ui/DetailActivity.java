package com.ma.traveldroid.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ma.traveldroid.R;
import com.ma.traveldroid.data.CountryContract;
import com.ma.traveldroid.data.CountryDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = DetailActivity.class.getName();

    private final int LOADER_INIT = 1;
    private Uri mContentUri;

    @BindView(R.id.country_name_textview)
    AutoCompleteTextView mCountryName;
    @BindView(R.id.visited_period_textview)
    EditText mVisitedPeriod;
    @BindView(R.id.save_button)
    Button mSave;

    //TODO cannot get data at row 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setupAutoCompleteTextView();

        buttonClick();

        mContentUri = getIntent().getData();
        if (mContentUri == null) {
            // FAB button is clicked

            Log.i(TAG, "FAB BUTTON IS CLICKED");

            //change title
            getSupportActionBar().setTitle(R.string.addNewCountry);

            // since a user has clicked a FAB button
            // no need for the menu that contains 'Delete' action.
            invalidateOptionsMenu();
        } else {

            Log.i(TAG,"RECYCLERVIEW ITEM IS CLICKED");

            // Recyclerview item is clicked
            getSupportActionBar().setTitle(R.string.editCountry);
            getSupportLoaderManager().initLoader(LOADER_INIT, null, this);
        }

    }

    private void buttonClick() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCountry();
            }
        });
    }


    private void saveCountry() {

        String countryName = mCountryName.getText().toString().trim();
        String visitedPeriod = mVisitedPeriod.getText().toString().trim();


        // if country name is empty display a toast message
        if (TextUtils.isEmpty(countryName)) {
            Toast.makeText(this, R.string.country_name_toast, Toast.LENGTH_SHORT).show();

        } else if(existsInDb(countryName)){
            // if this country name is already added to the database
            Toast.makeText(this, "You have already added this country.", Toast.LENGTH_SHORT).show();
        } else {


            ContentValues contentValues = new ContentValues();
            contentValues.put(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME, countryName);
            contentValues.put(CountryContract.CountryEntry.COLUMN_VISITED_PERIOD, visitedPeriod);

            if (mContentUri == null) {
                // if fab button is clicked then insert new product
                Uri insertedProductUri = getContentResolver().insert(CountryContract.CountryEntry.CONTENT_URI, contentValues);

                if (insertedProductUri == null) {
                    Toast.makeText(this, R.string.error_in_inserting, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.country_added, Toast.LENGTH_SHORT).show();
                }

            } else {
                // if recyclerview item is clicked then update product data
                int updatedProductsRowNumber = getContentResolver().update(mContentUri, contentValues, null, null);
                showToastMessage(updatedProductsRowNumber, "update");
            }
            finish();
        }
    }

    private boolean existsInDb(String countryName) {

        CountryDbHelper countryDbHelper = new CountryDbHelper(this);

        SQLiteDatabase db = countryDbHelper.getReadableDatabase();

        String[] columns = {CountryContract.CountryEntry.COLUMN_COUNTRY_NAME};
        String selection = CountryContract.CountryEntry.COLUMN_COUNTRY_NAME + " =?";
        String[] selectionArgs = { countryName };
        String limit = "1";

        Cursor cursor = db.query(CountryContract.CountryEntry.TABLE_NAME_COUNTRIES, columns, selection, selectionArgs, null, null, null, limit);
        return (cursor.getCount() > 0);
    }


    private void showToastMessage(int receivedRowNumber, String methodName) {
        if (receivedRowNumber == 0) {
            Toast.makeText(this, getString(R.string.country_failed) + " " + methodName + " product",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.country_success) + " " + methodName + " success",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void setupAutoCompleteTextView() {
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        mCountryName.setAdapter(adapter);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // If a user has clicked FAB button => hide the 'Delete' menu item
        if (mContentUri == null) {
            MenuItem deleteItem = menu.findItem(R.id.delete);
            deleteItem.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_delete_confirmation);
        builder.setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the country data
                deleteCountryData();
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so close the alert dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteCountryData(){
        if (mContentUri != null) {
            int deletedRowNumber = getContentResolver().delete(mContentUri, null, null);
            showToastMessage(deletedRowNumber, getResources().getString(R.string.action_delete));
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CountryContract.CountryEntry._ID,
                CountryContract.CountryEntry.COLUMN_COUNTRY_NAME,
                CountryContract.CountryEntry.COLUMN_VISITED_PERIOD
        };
        return new CursorLoader(this, mContentUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // get user clicked listItem index from db
        // get text of that index from db

        if (cursor.moveToNext()) {
            int countryIndex = cursor.getColumnIndexOrThrow(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME);
            int visitedPeriodIndex = cursor.getColumnIndexOrThrow(CountryContract.CountryEntry.COLUMN_VISITED_PERIOD);

            String countryNameText = cursor.getString(countryIndex);
            String visitedPeriodText = cursor.getString(visitedPeriodIndex);

            mCountryName.setText(countryNameText);
            mVisitedPeriod.setText(visitedPeriodText);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // reset values
        mCountryName.setText("");
        mVisitedPeriod.setText("");
    }
}
