package com.ma.bakingrecipes.ui.detail.steps;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ma.bakingrecipes.R;

public class StepDescriptionActivity extends AppCompatActivity {

    private final String TAG = StepDescriptionActivity.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String KEY_DESCRIPTION_NUMBER = "description_number";

    private String recipeName;
    private int descriptionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);

        if (getIntent() != null && getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(KEY_RECIPE_NAME)) {

            Log.d(TAG, "passed recipe name: " + getIntent().getExtras().getString(KEY_RECIPE_NAME));
            recipeName = getIntent().getExtras().getString(KEY_RECIPE_NAME);
            descriptionNumber = getIntent().getExtras().getInt(KEY_DESCRIPTION_NUMBER);
        }

        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", recipeName);
        bundle.putInt(KEY_DESCRIPTION_NUMBER, descriptionNumber);
        StepDescriptionFragment fragment = new StepDescriptionFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_description_container, fragment)
                .commit();
    }
}
