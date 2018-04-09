package com.ma.bakingrecipes.ui.detail.ingredients;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ma.bakingrecipes.R;

public class IngredientsActivity extends AppCompatActivity {

    private final String TAG = IngredientsActivity.class.getSimpleName();
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

//        if (getIntent().hasExtra("recipe_name")) {
//            recipeName = getIntent().getStringExtra("recipe_name");
//            Log.v(TAG, "recipe name - " + recipeName);
//        }
//
//
//        Bundle bundle = new Bundle();
//        bundle.putString("recipe_name", recipeName);
//        IngredientFragment fragment = new IngredientFragment();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .add(R.id.ingredient_container, fragment)
//                .commit();
//

    }
}

