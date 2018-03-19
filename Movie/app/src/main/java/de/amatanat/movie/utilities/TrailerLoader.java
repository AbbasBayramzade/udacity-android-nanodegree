package de.amatanat.movie.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import de.amatanat.movie.data.Model;

/**
 * Created by amatanat.
 */

public class TrailerLoader extends AsyncTaskLoader<List<Model>> {

    private String mUrl;

    public TrailerLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Model> loadInBackground() {
        // check if string url is null or not
        if (mUrl == null){
            return null;
        }

        return NetworkUtils.fetchTrailerData(mUrl);
    }
}
