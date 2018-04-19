package com.ma.bakingrecipes.utilities;

import android.content.Context;

import com.ma.bakingrecipes.AppExecutor;
import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.data.database.BakingDatabase;
import com.ma.bakingrecipes.data.network.RecipeNetworkDataSource;
import com.ma.bakingrecipes.data.network.RecipeService;
import com.ma.bakingrecipes.data.network.RetrofitClient;
import com.ma.bakingrecipes.ui.detail.SharedViewModelFactory;
import com.ma.bakingrecipes.ui.list.MainViewModelFactory;

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
                    provideNetworkDataSource();
            return RecipeRepository.getInstance(networkDataSource,
                    database.recipeDao(), executor);
    }

    private static RecipeNetworkDataSource provideNetworkDataSource() {
        return RecipeNetworkDataSource.getInstance();
    }

    public static SharedViewModelFactory provideDetailViewModelFactory(Context context, String name) {
        RecipeRepository repository = provideRepository(context.getApplicationContext());
        return new SharedViewModelFactory(repository, name);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        RecipeRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }
}
