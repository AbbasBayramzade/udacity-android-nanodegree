package de.amatanat.popularmovies.ui.list;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import de.amatanat.popularmovies.data.MovieRepository;

/**
 * Created by amatanat.
 */

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieRepository repository;

    public MainViewModelFactory(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(repository);
    }
}
