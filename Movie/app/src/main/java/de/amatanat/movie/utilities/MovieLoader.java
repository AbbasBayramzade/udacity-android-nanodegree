package de.amatanat.movie.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import de.amatanat.movie.data.Movie;

/**
 * Created by amatanat.
 */

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    private String mUrl;

    public MovieLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {

        // check if string url is null or not
        if (mUrl == null){
            return null;
        }

        return NetworkUtils.fetchData(mUrl);
    }
}
