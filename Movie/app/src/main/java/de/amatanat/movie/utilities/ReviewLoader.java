package de.amatanat.movie.utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import de.amatanat.movie.data.Review;

/**
 * Created by amatanat.
 */

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private String mUrl;

    public ReviewLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {
        // check if string url is null or not
        if (mUrl == null){
            return null;
        }

        return NetworkUtils.fetchReviewData(mUrl);
    }
}
