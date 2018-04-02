package com.ma.bakingrecipes.data.network;

import com.ma.bakingrecipes.model.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by amatanat.
 */

public interface RecipeService {

    @GET("/android-baking-app-json")
    Call<Recipe[]> getRecipes();
}
