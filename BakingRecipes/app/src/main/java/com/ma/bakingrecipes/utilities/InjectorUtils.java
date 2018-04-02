package com.ma.bakingrecipes.utilities;

import android.content.Context;

import com.ma.bakingrecipes.AppExecutor;
import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.data.database.BakingDatabase;
import com.ma.bakingrecipes.data.network.RecipeNetworkDataSource;
import com.ma.bakingrecipes.data.network.RecipeService;
import com.ma.bakingrecipes.data.network.RetrofitClient;
import com.ma.bakingrecipes.ui.detail.DetailViewModelFactory;

/**
 * Created by amatanat.
 */

public class InjectorUtils {

    private static final String BASE_URL = "http://go.udacity.com/";

    public static RecipeService provideRecipeService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }

    public static RecipeRepository provideRepository(Context context) {
        BakingDatabase database = BakingDatabase.getInstance(context.getApplicationContext());
            AppExecutor executor = AppExecutor.getInstance();
            RecipeNetworkDataSource networkDataSource =
                    RecipeNetworkDataSource.getInstance();
            return RecipeRepository.getInstance(networkDataSource,
                    database.recipeDao(), executor);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, String name) {
        RecipeRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, name);
    }
}
