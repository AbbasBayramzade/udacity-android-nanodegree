package com.ma.bakingrecipes.ui.detail.ingredients;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.ui.detail.DetailActivityViewModel;

/**
 * Created by amatanat.
 */

public class IngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository repository;
    private String name;

    public IngredientViewModelFactory(RecipeRepository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new IngredientActivityViewModel(repository, name);
    }
}
