package com.ma.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.ma.bakingrecipes.R;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    private static final String TAG = WidgetProvider.class.getName();

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
    }

    @Override
    public void onDisabled(Context context) {
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

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}

