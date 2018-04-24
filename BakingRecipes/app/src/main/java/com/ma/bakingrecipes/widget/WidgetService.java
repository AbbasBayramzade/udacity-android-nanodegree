package com.ma.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("WIDGET SERVICE","call");
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        String recipeName = intent.getStringExtra("recipe_name");
        Log.v("RemoteViewsFactory","recipe name " + recipeName);
        return (new ViewFactory(this.getApplicationContext(),
                intent));
    }

}
