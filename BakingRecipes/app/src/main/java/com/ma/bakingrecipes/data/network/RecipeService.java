package com.ma.bakingrecipes.data.network;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by amatanat.
 */

public interface RecipeService {

    @GET("/android-baking-app-json")
    Call<RecipeResponse> getRecipes();
}
