package com.ma.traveldroid;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ma.traveldroid.roomDb.CountryDao;
import com.ma.traveldroid.roomDb.CountryDatabase;
import com.ma.traveldroid.roomDb.CountryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ListRemoveViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ListRemoveViewsFactory.class.getName();

    private Context mContext;
    private List<CountryEntry> mCountriesList = new ArrayList<>();

    public ListRemoveViewsFactory(final Context context){
        mContext = context;

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {
                        new DownloadCountries(context).execute();
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
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {

        Log.d(TAG,
                "countries: " + mCountriesList.size());
        return mCountriesList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(),
                R.layout.row);
        Log.d(TAG,
                "text in getViewAt: " + mCountriesList.get(position).getCountryName());

        // set text of listview item at corresponding position
        row.setTextViewText(android.R.id.text1, mCountriesList.get(position).getCountryName());

        return (row);
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

    private class DownloadCountries extends AsyncTask<Void, Void,List<CountryEntry> > {

        private final String TAG = DownloadCountries.class.getName();

        private Context context;

        private DownloadCountries(Context context) {
            this.context = context;
        }

        @Override
        protected List<CountryEntry> doInBackground(Void... voids) {
            Log.v(TAG, "doinBackground call ***********");
            CountryDatabase database = CountryDatabase.getInstance(context);
            CountryDao dao = database.countryDao();
            return dao.getAllCountries();
        }

        @Override
        protected void onPostExecute(List<CountryEntry> countryEntryList) {
            // update countries list
            mCountriesList = countryEntryList;
            // notify widget manager about data set change
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, CountryAppWidget.class)), R.id.words);
        }
    }
}
