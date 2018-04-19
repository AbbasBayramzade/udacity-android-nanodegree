package com.ma.bakingrecipes.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class ViewFactory implements RemoteViewsService.RemoteViewsFactory, DownloadRecipeTask.GetRecipeListener {

    private static final String TAG = ViewFactory.class.getName();

    private List<Ingredient> ingredientList = new ArrayList<>();
    private Context context;
    private Recipe recipe;
    private int mAppWidgetId;


    public ViewFactory(Context context, Intent intent) {
        this.context = context;
    }


    @Override
    public void onRemoteCallComplete(Recipe recipe) {
        this.recipe = recipe;
        Log.v(TAG, "recipe name: " + recipe.getName());
        ingredientList = this.recipe.getIngredients();
        Log.v(TAG, "ingredient: " + ingredientList.get(0).getIngredient());
    }

    @Override
    public void onCreate() {
        new DownloadRecipeTask(this.context, this).execute();
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7" +
                "ingredient: " + ingredientList.size());
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews row=new RemoteViews(context.getPackageName(),
                R.layout.row);

        Log.d(TAG, "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7" +
                "ingredient test: " + ingredientList.get(position).getIngredient());

        row.setTextViewText(R.id.text, ingredientList.get(position).getIngredient());

        Intent i = new Intent();
        Bundle extras=new Bundle();

        extras.putString(RecipeWidgetProvider.EXTRA_RECIPE, ingredientList.get(position).getIngredient());
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.text, i);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
