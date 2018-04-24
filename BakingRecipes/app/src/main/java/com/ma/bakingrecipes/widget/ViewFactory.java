package com.ma.bakingrecipes.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.data.database.BakingDatabase;
import com.ma.bakingrecipes.data.database.RecipeDao;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ViewFactory.class.getName();
    private List<Ingredient> ingredientList = new ArrayList<>();

    private Context ctxt;
    private String recipeName;


    ViewFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        recipeName = intent.getStringExtra("recipe_name");

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        new DownloadTask(ctxt).execute(recipeName);
                    } catch (Exception ignored) {
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 60000);

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        Log.d(TAG,
                "ingredient: " + ingredientList.size());
        return ingredientList.size();
    }


    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.row);
        Log.d(TAG,
                "text in getViewAt: " + ingredientList.get(position).getIngredient());

        // set text of listview item at corresponding position
        row.setTextViewText(android.R.id.text1, ingredientList.get(position).getIngredient());

        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
    }


    private class DownloadTask extends AsyncTask<String, Void, Recipe> {

        private final String TAG = DownloadTask.class.getName();

        @SuppressLint("StaticFieldLeak")
        private Context context;

        private DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected Recipe doInBackground(String... text) {
            Log.v(TAG, "doinBackground call ***********");
            BakingDatabase database = BakingDatabase.getInstance(context);
            RecipeDao re = database.recipeDao();
            return re.getRecipe(text[0]);
        }

        @Override
        protected void onPostExecute(Recipe recipe) {
            // update ingredients list
            ingredientList = recipe.getIngredients();
            // notify widget manager about data set change
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctxt);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(
                    new ComponentName(ctxt, WidgetProvider.class)), R.id.words);
        }
    }
}
