package de.amatanat.popularmovies.data.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by amatanat.
 */
@Entity(tableName = "movies")
public class MovieEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_title")
    private String title;

    @ColumnInfo(name = "movie_image")
    private String image;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "movie_release_date")
    private String releaseDate;

    @ColumnInfo(name = "movie_rating")
    private double rating;

    @ColumnInfo(name = "movie_description")
    private String description;

    // This constructor is used by Room
    public MovieEntry(int id, String title, String image, int movieId, String releaseDate, double rating, String description) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    // This constructor is used by JSONParser
    @Ignore
    public MovieEntry(String title, String image, int movieId, String releaseDate, double rating, String description) {
        this.title = title;
        this.image = image;
        this.movieId = movieId;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
