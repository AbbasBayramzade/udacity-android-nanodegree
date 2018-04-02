package com.ma.bakingrecipes.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ma.bakingrecipes.data.RecipeRepository;

/**
 * Created by amatanat.
 */

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeRepository repository;
    private String name;

    public DetailViewModelFactory(RecipeRepository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailActivityViewModel(repository, name);
    }
}
