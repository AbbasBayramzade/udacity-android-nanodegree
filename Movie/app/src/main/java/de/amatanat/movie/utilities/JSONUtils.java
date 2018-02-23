package de.amatanat.movie.utilities;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.amatanat.movie.data.Movie;

/**
 * Created by amatanat.
 */

public class JSONUtils {

    private static final String TAG = JSONUtils.class.getName();
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORI_TITLE = "original_title";
    private static final String KEY_MOVIE_ID = "id";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_RATING = "vote_average";
    private static final String KEY_DESCRIPTION = "overview";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_POSTER_PATH = "poster_path";

    public static List<Movie> parseMovieJson(String json) {

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
    private static List<Movie> getListFromJsonArray(JSONArray jsonArray) {
        List<Movie> result = new ArrayList<>();
        // check if array isn't empty
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                // get jsonobject at position i
                JSONObject movie = jsonArray.optJSONObject(i);
                // get 'title' from jsonobject
                String title = movie.optString(KEY_TITLE);
                Log.i(TAG, "title = " + title);
                // String originalTile = movie.optString(KEY_ORI_TITLE);
                String imageEnd = movie.optString(KEY_POSTER_PATH);
                Log.i(TAG, "imageEnd = " + imageEnd);
                String image = "http://image.tmdb.org/t/p/w185/" + imageEnd;

                int movieId = movie.optInt(KEY_MOVIE_ID);
                Log.i(TAG, "movieId = " + movieId);

                String releaseDate = movie.optString(KEY_RELEASE_DATE);
                Log.i(TAG, "releaseDate = " + releaseDate);

                int rating = movie.optInt(KEY_RATING);
                Log.i(TAG, "rating = " + rating);

                String description = movie.optString(KEY_DESCRIPTION);
                Log.i(TAG, "description = " + description);
                result.add(
                        new Movie(movieId,title, image, releaseDate, rating, description)
                );
            }
        }
        return result;
    }
}
