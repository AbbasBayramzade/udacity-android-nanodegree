package com.ma.bakingrecipes.utilities;

import com.ma.bakingrecipes.data.network.RecipeService;
import com.ma.bakingrecipes.data.network.RetrofitClient;

/**
 * Created by amatanat.
 */

public class InjectorUtils {

    private static final String BASE_URL = "http://go.udacity.com/";

    public static RecipeService provideRecipeService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }
}
