package com.ma.bakingrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.ui.list.MainActivity;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    public static String EXTRA_RECIPE=
            "com.ma.bakingrecipes.widget.RECIPE";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        Intent intent= new Intent(context, WidgetService.class);

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(appWidgetId,R.id.widget_listview,intent);

        Intent clickIntent=new Intent(context, MainActivity.class);
        PendingIntent clickPI=PendingIntent
                .getActivity(context, 0,
                        clickIntent,
                        0);

       views.setPendingIntentTemplate(R.id.widget_listview, clickPI);
      //  views.setOnClickPendingIntent(R.id.widget_listview, clickPI);

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

