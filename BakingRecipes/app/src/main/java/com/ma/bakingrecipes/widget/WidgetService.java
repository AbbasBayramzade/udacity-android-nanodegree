package com.ma.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.v("WIDGET SERVICE","call");
        return (new ViewFactory(this.getApplicationContext(),
                intent));
    }

}
