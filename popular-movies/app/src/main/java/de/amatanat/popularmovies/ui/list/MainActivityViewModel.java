package de.amatanat.popularmovies.ui.list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import de.amatanat.popularmovies.data.MovieRepository;
import de.amatanat.popularmovies.data.database.GridViewMovieEntry;

/**
 * Created by amatanat.
 */

class MainActivityViewModel extends ViewModel {

    private LiveData<List<GridViewMovieEntry>> movies;
    private MovieRepository repository;

    public MainActivityViewModel(MovieRepository repository) {
        this.repository = repository;
        //TODO
        //movies = repository.getCurrentMovies();

    }

    public LiveData<List<GridViewMovieEntry>> getCurrentMovies() {
        return movies;
    }
}
