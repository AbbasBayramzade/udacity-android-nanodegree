package com.ma.bakingrecipes.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ma.bakingrecipes.AppExecutor;
import com.ma.bakingrecipes.data.database.RecipeDao;
import com.ma.bakingrecipes.data.database.RecyclerViewRecipeEntry;
import com.ma.bakingrecipes.data.network.RecipeNetworkDataSource;
import com.ma.bakingrecipes.model.Recipe;

import java.util.List;


/**
 * Created by amatanat.
 */

public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static RecipeRepository sInstance;
    private final RecipeNetworkDataSource recipeNetworkDataSource;
    private final RecipeDao recipeDao;
    private final AppExecutor executor;
    private boolean initialized = false;

    private RecipeRepository(RecipeNetworkDataSource recipeNetworkDataSource,
                             RecipeDao recipeDao,
                             AppExecutor executor){
        this.recipeNetworkDataSource = recipeNetworkDataSource;
        this.recipeDao = recipeDao;
        this.executor = executor;

        LiveData<Recipe[]> networkData = this.recipeNetworkDataSource.getRecipes();
        networkData.observeForever(newRecipesFromNetwork -> {
            this.executor.diskIO().execute(() -> {
                this.recipeDao.insert(newRecipesFromNetwork);
                Log.d(TAG, "New values inserted into database");
            });
        });
    }

    public synchronized static RecipeRepository getInstance(RecipeNetworkDataSource recipeNetworkDataSource,
                                                            RecipeDao recipeDao,
                                                            AppExecutor executor) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeRepository(recipeNetworkDataSource,recipeDao,executor);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<List<RecyclerViewRecipeEntry>> getRecipes(){
        fetchRecipes();
        return recipeDao.getAllRecipes();
    }

    public LiveData<Recipe> getRecipeByName(String name){
        fetchRecipes();
        return recipeDao.getRecipeByName(name);
    }

    private synchronized void fetchRecipes() {

        if (initialized) return;
        initialized = true;

        this.executor.diskIO().execute(() -> {
            startRecipeNetworkDataSource();
            Log.d(TAG, "start recipe network data source");

        });
    }

    private void startRecipeNetworkDataSource() {
        recipeNetworkDataSource.loadRecipes();
    }

}
