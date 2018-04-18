package com.ma.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.data.RecipeRepository;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<String> ingredientList = new ArrayList<>();
    private int appWidgetId;
    private Context context;


    public ViewFactory(Context context, Intent intent) {
        this.context = context;
        // TODO: get ingredients list for one of the recipes.
//        RecipeRepository recipeRepository = InjectorUtils.provideRepository(context);
//        ingredientList = recipeRepository.getRecipeByName("Nutella Pie")
//                .getValue().getIngredients();

        ingredientList.add("ing1");
        ingredientList.add("ing2");


        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(context.getPackageName(),
                R.layout.row);

        row.setTextViewText(android.R.id.text1, ingredientList.get(position));

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(RecipeWidgetProvider.EXTRA_RECIPE, ingredientList.get(position));
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
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
