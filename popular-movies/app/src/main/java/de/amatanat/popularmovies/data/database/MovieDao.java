package de.amatanat.popularmovies.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by amatanat.
 */

@Dao
public interface MovieDao {

    /***
     * Insert any number of movie entries into the database
     * When the app re-downloads movies, replaces old movie data with the new data
     * @param movieEntries a list of movies
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntry... movieEntries);

    /***
     * @return movies list
     */
    @Query("SELECT * FROM movies")
    List<MovieEntry> getAllMovies();

}
