package com.ma.traveldroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ma.traveldroid.R;
import com.ma.traveldroid.roomDb.CountryDatabase;
import com.ma.traveldroid.roomDb.CountryEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getName();

    public static final String EXTRA_ID = "extra_id";
    public static final String COUNTRY_ID = "country_id";

    // Constant for default country id to be used when not in update mode
    private static final int DEFAULT_ID = -1;

    private CountryDatabase mCountryDatabase;
    private int mCountryId = DEFAULT_ID;

    @BindView(R.id.country_name_textview)
    AutoCompleteTextView mCountryName;
    @BindView(R.id.visited_period_textview)
    EditText mVisitedPeriod;
    @BindView(R.id.save_button)
    Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(COUNTRY_ID)) {
            mCountryId = savedInstanceState.getInt(COUNTRY_ID, DEFAULT_ID);
        }

        mCountryDatabase = CountryDatabase.getInstance(getApplicationContext());

        setupAutoCompleteTextView();

        buttonClick();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ID)) {
            mSave.setText(R.string.text_update);
            getSupportActionBar().setTitle(R.string.editCountry);
            if (mCountryId == DEFAULT_ID) {
                mCountryId = intent.getIntExtra(EXTRA_ID, DEFAULT_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        final CountryEntry countryEntry = mCountryDatabase.countryDao().getCountryById(mCountryId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(countryEntry);
                            }
                        });
                    }
                });
            }
        } else {
            getSupportActionBar().setTitle(R.string.addNewCountry);
        }

    }

    private void updateUI(CountryEntry countryEntry) {
        if (countryEntry == null)
            return;

        mCountryName.setText(countryEntry.getCountryName());
        mVisitedPeriod.setText(countryEntry.getVisitedPeriod());
    }

    private void buttonClick() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCountry();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(COUNTRY_ID, mCountryId);
        super.onSaveInstanceState(outState);
    }


    private void saveCountry() {

        String countryName = mCountryName.getText().toString().trim();
        String visitedPeriod = mVisitedPeriod.getText().toString().trim();

        final CountryEntry countryEntry = new CountryEntry(countryName, visitedPeriod);

        // TODO before inserting into db check if already exists in db
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mCountryId == DEFAULT_ID) {
                    // insert new country into db
                    mCountryDatabase.countryDao().insertCountry(countryEntry);
                } else {
                    //update country data in db
                    countryEntry.setId(mCountryId);
                    mCountryDatabase.countryDao().updateCoutry(countryEntry);
                }
                finish();
            }
        });
    }

    //TODO
    private boolean existsInDb(String countryName) {
        return true;
    }


    private void showToastMessage(String methodName) {
        Toast.makeText(this, getString(R.string.country_success) + " " + methodName + " success",
                Toast.LENGTH_SHORT).show();
    }

    private void setupAutoCompleteTextView() {
        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countries);
        mCountryName.setAdapter(adapter);
    }
}
