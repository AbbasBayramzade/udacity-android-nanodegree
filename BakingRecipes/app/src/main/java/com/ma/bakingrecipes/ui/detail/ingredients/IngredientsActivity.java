package com.ma.bakingrecipes.ui.detail.ingredients;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ma.bakingrecipes.R;

import butterknife.ButterKnife;

public class IngredientsActivity extends AppCompatActivity {

    private final String TAG = IngredientsActivity.class.getSimpleName();
    private final String KEY_RECIPE_NAME = "recipe_name";
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getExtras() != null &&
                getIntent().getExtras().containsKey(KEY_RECIPE_NAME)) {

            Log.d(TAG, "passed recipe name: " + getIntent().getExtras().getString(KEY_RECIPE_NAME));
            recipeName = getIntent().getExtras().getString(KEY_RECIPE_NAME);
        }

        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", recipeName);
        IngredientFragment fragment = new IngredientFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}

