package com.ma.bakingrecipes.data;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.ma.bakingrecipes.data.network.RecipeNetworkDataSource;
import com.ma.bakingrecipes.model.Recipe;

/**
 * Created by amatanat.
 */

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;
    private final RecipeNetworkDataSource recipeNetworkDataSource;

    private RecipeRepository(RecipeNetworkDataSource recipeNetworkDataSource){
        this.recipeNetworkDataSource = recipeNetworkDataSource;
        LiveData<Recipe[]> networkData = this.recipeNetworkDataSource.getRecipes();
        // TODO
//        networkData.observeForever(newRecipesFromNetwork -> {
//                // TODO delete old data
//                // TODO Insert our new recipe data into the database
//                Log.d(TAG, "New values inserted");
//        });
    }

    public synchronized static RecipeRepository getInstance(RecipeNetworkDataSource recipeNetworkDataSource) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeRepository(recipeNetworkDataSource);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }


}
