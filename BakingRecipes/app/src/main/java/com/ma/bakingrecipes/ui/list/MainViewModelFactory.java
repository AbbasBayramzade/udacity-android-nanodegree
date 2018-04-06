package com.ma.bakingrecipes.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ma.bakingrecipes.data.RecipeRepository;

/**
 * Created by amatanat.
 */

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeRepository repository;

    public MainViewModelFactory(RecipeRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(repository);
    }
}
