package com.ma.bakingrecipes.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.ui.detail.ingredients.IngredientFragment;
import com.ma.bakingrecipes.ui.detail.ingredients.IngredientsActivity;
import com.ma.bakingrecipes.ui.detail.steps.StepDescriptionActivity;
import com.ma.bakingrecipes.ui.detail.steps.StepDescriptionFragment;

public class DetailActivity extends AppCompatActivity implements ItemFragment.OnItemClickListener {

    private final String TAG = DetailActivity.class.getName();

    private final String KEY_POSITION = "CLICKED_POSITION";
    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String KEY_DESCRIPTION_NUMBER = "description_number";

    private boolean isTablet;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(savedInstanceState != null ){
            recipeName = savedInstanceState.getString(KEY_RECIPE_NAME);
        } else{
            if (getIntent() != null && getIntent().getExtras() != null &&
                    getIntent().getExtras().containsKey(KEY_RECIPE_NAME)) {

                Log.d(TAG, "passed recipe name: " + getIntent().getExtras().getString(KEY_RECIPE_NAME));
                recipeName = getIntent().getExtras().getString(KEY_RECIPE_NAME);
            }
        }

        Log.d(TAG, "RECIPE NAME " + recipeName);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECIPE_NAME, recipeName);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.item_container, fragment)
                .commit();

        // check if device is a tablet
        isTablet = getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onItemSelected(int position) {
        if (isTablet) {
            Bundle bundle = new Bundle();
            bundle.putString("recipe_name", recipeName);
            bundle.putInt(KEY_DESCRIPTION_NUMBER, position);

            if (position == 0) {
                IngredientFragment fragment = new IngredientFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            } else {
                StepDescriptionFragment fragment = new StepDescriptionFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        } else {
            Intent intent;
            if (position == 0) {
                intent = new Intent(DetailActivity.this, IngredientsActivity.class);
                Log.d(TAG, "OPEN ING");

            } else {
                intent = new Intent(DetailActivity.this, StepDescriptionActivity.class);
                Log.d(TAG, "OPEN STEPS");
            }
            intent.putExtra(KEY_DESCRIPTION_NUMBER, position);
            intent.putExtra(KEY_RECIPE_NAME, recipeName);
            startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_RECIPE_NAME, recipeName);
        super.onSaveInstanceState(outState);
    }
}
