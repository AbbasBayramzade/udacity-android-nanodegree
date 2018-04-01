package com.ma.bakingrecipes.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amatanat.
 */

public class RecipeNetworkDataSource {

    private static final String TAG = RecipeNetworkDataSource.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static RecipeNetworkDataSource sInstance;
    private final RecipeService mRecipeService;

    // LiveData storing the latest downloaded recipes
    private final MutableLiveData<Recipe[]> mDownloadedRecipes;


    private RecipeNetworkDataSource() {
        mRecipeService = InjectorUtils.provideRecipeService();
        mDownloadedRecipes = new MutableLiveData<>();
    }

    public synchronized static RecipeNetworkDataSource getInstance() {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeNetworkDataSource();
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public LiveData<Recipe[]> getRecipes() {
        return mDownloadedRecipes;
    }

    public void loadRecipes() {
        mRecipeService.getRecipes().enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {

                if(response.isSuccessful()) {
                    Recipe[] recipes = response.body().getRecipes();
                    RecipeResponse recipeResponse = new RecipeResponse(recipes);
                    Log.d(TAG, "data loaded from API");

                    mDownloadedRecipes.postValue(recipeResponse.getRecipes());

                } else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Log.d(TAG,"status code error: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                // TODO show toast error
                Log.d(TAG, "error loading from API");

            }
        });
    }
}
