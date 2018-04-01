package com.ma.bakingrecipes.data.network;

import android.support.annotation.NonNull;

import com.ma.bakingrecipes.model.Recipe;

/**
 * Created by amatanat.
 */

public class RecipeResponse {
    @NonNull
    private final Recipe[] recipes;

    public RecipeResponse(@NonNull final Recipe[] recipes) {
        this.recipes = recipes;
    }

    public Recipe[] getRecipes() {
        return recipes;
    }
}

