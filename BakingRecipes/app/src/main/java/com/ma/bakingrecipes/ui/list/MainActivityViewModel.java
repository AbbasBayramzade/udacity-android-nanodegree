package com.ma.bakingrecipes.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.data.database.RecyclerViewRecipeEntry;

import java.util.List;

/**
 * Created by amatanat.
 */

public class MainActivityViewModel extends ViewModel {

    // Recipe list the user looking at
    private LiveData<List<RecyclerViewRecipeEntry>> recipes;
    private final RecipeRepository repository;

    public MainActivityViewModel(RecipeRepository repository) {
        this.repository = repository;
        recipes = this.repository.getRecipes();

    }

    public LiveData<List<RecyclerViewRecipeEntry>> getRecipes() {
        return recipes;
    }

}
