package com.ma.bakingrecipes.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ma.bakingrecipes.R;

// source https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/appwidget?autodive=0%2F%2F%2F/

public class WidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "AppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget";

    int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public WidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.widget_configure);

        // Set layout size of activity
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // widget's host activity will cancel widget placement when the user presses a back button
        setResult(RESULT_CANCELED);

        final ListView listView = findViewById(R.id.list);

        // recipes name for the ListView
        String[] values = new String[] { "Nutella Pie",
                "Brownies",
                "Yellow Cake",
                "Cheesecake"
        };

        // ListView adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listView.setAdapter(adapter);

        // ListView item click
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Take clicked item's value
            String  recipeName  = (String) listView.getItemAtPosition(position);
            // create widget for the corresponding recipe
            createWidget(getApplicationContext(), recipeName);
        });

        // Find the widget id from the intent that launched this activity, i.e App Widget Host Activity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    private void createWidget(Context context, String recipe) {
        // Store a recipe name string locally
        saveTitlePref(context, appWidgetId, recipe);

        // update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREF_PREFIX_KEY + appWidgetId, "Nutella Pie");
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }
}
