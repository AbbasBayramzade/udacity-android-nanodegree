package de.amatanat.popularmovies.data.network;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by amatanat.
 */

final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String ENDPOINT_POPULAR = "popular?api_key=";
    private static final String ENDPOINT_TOP_RATED = "top_rated?api_key=";
    private static final String API_KEY = "a9f1d387a51d7d793eeaf0f0114eb1e1";


    static URL getUrl(String endpoint) {
        switch (endpoint){
            case "popular":
                return buildPopularMovieUrl();
            case "top_rated":
                return buildTopRatedMovieUrl();
            default:
                return null;
        }
    }

    private static URL buildPopularMovieUrl(){
        Uri queryUri = Uri.parse(BASE_URL + ENDPOINT_POPULAR +API_KEY).buildUpon()
             .build();
        try {
            URL queryUrl = new URL(queryUri.toString());
            Log.d(TAG, "URL: " + queryUrl);
            return queryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static URL buildTopRatedMovieUrl(){
        Uri queryUri = Uri.parse(BASE_URL + ENDPOINT_TOP_RATED +API_KEY).buildUpon()
                .build();
        try {
            URL queryUrl = new URL(queryUri.toString());
            Log.d(TAG, "URL: " + queryUrl);
            return queryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String httpResponse = null;
            if (hasInput) {
                httpResponse = scanner.next();
            }
            scanner.close();
            return httpResponse;
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
