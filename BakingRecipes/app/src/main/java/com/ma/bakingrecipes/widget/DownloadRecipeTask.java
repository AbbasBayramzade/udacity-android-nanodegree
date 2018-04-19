package com.ma.bakingrecipes.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ma.bakingrecipes.data.database.BakingDatabase;
import com.ma.bakingrecipes.data.database.RecipeDao;
import com.ma.bakingrecipes.model.Recipe;

/**
 * Created by amatanat.
 */

public class DownloadRecipeTask extends AsyncTask<Void, Void, Recipe> {

    private static final String TAG = DownloadRecipeTask.class.getName();

    public interface GetRecipeListener {
        void onRemoteCallComplete(Recipe recipe);
    }

    private Context context;
    GetRecipeListener getRecipeListener;

    public DownloadRecipeTask(Context context, GetRecipeListener listener) {
        this.context = context;
        this.getRecipeListener = listener;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {
        Log.i(TAG, "doinBackground ");
        BakingDatabase database = BakingDatabase.getInstance(context);
        Log.i(TAG, "database");
        RecipeDao re = database.recipeDao();
        return re.getRecipe("Nutella Pie");
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        getRecipeListener.onRemoteCallComplete(recipe);
    }
}


