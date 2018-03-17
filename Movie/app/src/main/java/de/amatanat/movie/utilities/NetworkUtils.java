package de.amatanat.movie.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import de.amatanat.movie.data.Movie;
import de.amatanat.movie.data.Review;

/**
 * Created by amatanat.
 */

// Use this class to perform network operations

public class NetworkUtils {


    /**
     * Create and return URL from given string url
     */
    private static URL getUrl(String stringUrl){
        URL url = null;

        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e("Query", "MalformedURLException: ", e);
        }
        return  url;
    }

    /*
    * Make http request to the server and get response data
    */
    private static String getHttpResponse(URL url) throws IOException {
        String response = "";

        if (url == null){
            return null;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            // open url connection
            httpURLConnection = (HttpURLConnection) url.openConnection();

            // milliseconds
            httpURLConnection.setReadTimeout(10000);
            // milliseconds
            httpURLConnection.setConnectTimeout(15000);

            // set http request method to 'GET'
            httpURLConnection.setRequestMethod("GET");

            // connect to server
            httpURLConnection.connect();

            // check http response code
            if (httpURLConnection.getResponseCode() == 200){

                // get data from server
                inputStream = httpURLConnection.getInputStream();

                // get response data from inputstream
                response = readInputStream(inputStream);
            } else {
                Log.e("Query", "Response code " + httpURLConnection.getResponseCode());
            }
        }catch (IOException exception) {
            Log.e("Query", "IOException: " + exception);

        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return response;
    }

    /*
    * Read input stream and convert it into String
    */
    private static String readInputStream(InputStream inputStream) throws IOException {
        String result;
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null){

            // convert inputstream (byte) to character with {@link InputStreamReader} one byte at a time
            // with {@link BufferedReader} convert more than one byte at a time
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            while((result = bufferedReader.readLine()) != null) {

                // append each line to the end of stringbuilder
                stringBuilder.append(result);
            }
        }
        return stringBuilder.toString();
    }


    public static List<Movie> fetchData(String requestUrl) {

        try{
            Thread.sleep(2000);
        }catch (InterruptedException ie){
            Log.e("Query", "InterruptedException: " + ie);
        }

        // Create URL object
        URL url = getUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = getHttpResponse(url);
        } catch (IOException e) {
            Log.e("Query", "Problem making the HTTP request.", e);
        }

        // parse JSON response and return Movie list
        return JSONUtils.parseMovieJson(jsonResponse);
    }


    public static List<Review> fetchReviewData(String requestUrl) {

        try{
            Thread.sleep(2000);
        }catch (InterruptedException ie){
            Log.e("Query", "InterruptedException: " + ie);
        }

        // Create URL object
        URL url = getUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = getHttpResponse(url);
        } catch (IOException e) {
            Log.e("Query", "Problem making the HTTP request.", e);
        }

        // parse JSON response and return Movie list
        return JSONUtils.parseReviewJson(jsonResponse);
    }
}
