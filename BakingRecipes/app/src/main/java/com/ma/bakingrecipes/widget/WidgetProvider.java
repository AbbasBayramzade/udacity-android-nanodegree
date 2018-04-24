package com.ma.bakingrecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.ui.detail.DetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = WidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra("recipe_name", widgetText);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget);


        views.setTextViewText(R.id.widget_tv, widgetText);

        views.setRemoteAdapter(appWidgetId, R.id.words,
                svcIntent);

        Log.v("WIDGET PROVIDER", "adapter ***** " + widgetText);


        Intent clickIntent = new Intent(context, DetailActivity.class);
        clickIntent.putExtra("recipe_name", widgetText.toString());

        Log.v("WIDGET PROVIDER", "$$$$$$$$$$$$$$ " + widgetText.toString());
        PendingIntent clickPI = PendingIntent
                .getActivity(context, 0,
                        clickIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.words, clickPI);


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

