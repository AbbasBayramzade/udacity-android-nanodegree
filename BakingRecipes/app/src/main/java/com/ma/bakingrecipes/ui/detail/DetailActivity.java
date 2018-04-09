package com.ma.bakingrecipes.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
    private boolean isTablet;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (getIntent() != null && getIntent().getExtras() != null &&
                    getIntent().getExtras().containsKey(KEY_RECIPE_NAME)) {

            Log.d(TAG, "passed recipe name: " + getIntent().getExtras().getString(KEY_RECIPE_NAME));
            recipeName = getIntent().getExtras().getString(KEY_RECIPE_NAME);
        }

    }

    @Override
    public void onItemSelected(int position) {
        if (isTablet) {
            if (position == 0) {
                IngredientFragment fragment = new IngredientFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            } else {
                StepDescriptionFragment fragment = new StepDescriptionFragment();
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
            intent.putExtra(KEY_POSITION, position);
            intent.putExtra(KEY_RECIPE_NAME, recipeName);
            startActivity(intent);
        }

    }
}
