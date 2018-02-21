package de.amatanat.popularmovies.utilities;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.amatanat.popularmovies.data.database.MovieEntry;

/**
 * Created by amatanat.
 * Use this class to parse JSON string
 */

public class JSONUtils {

    private static final String KEY_TITLE = "title";
    private static final String KEY_ORI_TITLE = "original_title";
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_RATING = "vote_average";
    private static final String KEY_DESCRIPTION = "overview";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_POSTER_PATH = "poster_path";

    public static List<MovieEntry> parseMovieJson(String json) {

        if (TextUtils.isEmpty(json))
            return null;

        try {
            // get jsonobject from input string
            JSONObject jsonObject = new JSONObject(json);
            return getListFromJsonArray(jsonObject.optJSONArray(KEY_RESULTS));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method gets JSONArray as input and returns list of movies
     */
    private static List<MovieEntry> getListFromJsonArray(JSONArray jsonArray) {
        List<MovieEntry> result = new ArrayList<>();
        // check if array isn't empty
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                // get jsonobject at position i
                JSONObject movie = jsonArray.optJSONObject(i);
                // get 'title' from jsonobject
                String title = movie.optString(KEY_TITLE);
               // String originalTile = movie.optString(KEY_ORI_TITLE);
                String image = movie.optString(KEY_POSTER_PATH);
                int movieId = movie.optInt(KEY_MOVIE_ID);
                String releaseDate = movie.optString(KEY_RELEASE_DATE);
                int rating = movie.optInt(KEY_RATING);
                String description = movie.optString(KEY_DESCRIPTION);
                result.add(
                        new MovieEntry(title, image, movieId, releaseDate, rating, description)
                );
            }
        }
        return result;
    }
}
