package com.ma.bakingrecipes.ui.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.model.Recipe;

/**
 * Created by amatanat.
 */

public class SharedViewModel extends ViewModel {
    // Recipe data to which user looks
    private LiveData<Recipe> recipeLiveData;
    private RecipeRepository recipeRepository;
    private String name;

    public SharedViewModel(RecipeRepository recipeRepository, String name) {
        this.recipeRepository = recipeRepository;
        this.name = name;
        recipeLiveData = this.recipeRepository.getRecipeByName(this.name);
    }

    public LiveData<Recipe> getRecipe() {
        return recipeLiveData;
    }
}
