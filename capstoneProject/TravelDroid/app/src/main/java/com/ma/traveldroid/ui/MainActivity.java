package com.ma.traveldroid.ui;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ma.traveldroid.R;
import com.ma.traveldroid.data.CountryContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 100;
    private static final int LOADER_INIT = 200;

   // private MyRecyclerAdapter mMyRecyclerAdapter;
    private MyListCursorAdapter myListCursorAdapter;
    private String mMapContent;

    @BindView(R.id.countries_recyclerview)
    RecyclerView mCountriesRecyclerView;
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.countries_label)
    TextView mCountriesLabel;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.emptyview_image)
    LottieAnimationView mLottieAnimationView;
    @BindView(R.id.emptyview_subtitle)
    TextView mEmptyViewText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mLottieAnimationView.setAnimation("EmptyState.json");
        mLottieAnimationView.loop(true);
        mLottieAnimationView.playAnimation();

        if(savedInstanceState != null)
            mMapContent = savedInstanceState.getString("MAP_CONTENT");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mCountriesRecyclerView.setLayoutManager(layoutManager);
        mCountriesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //mMyRecyclerAdapter = new MyRecyclerAdapter(this, null);
        myListCursorAdapter = new MyListCursorAdapter(null);
        //mCountriesRecyclerView.setAdapter(mMyRecyclerAdapter);
        mCountriesRecyclerView.setAdapter(myListCursorAdapter);

        mCountriesRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {

                        Toast.makeText(MainActivity.this, "clicked pos: " + position, Toast.LENGTH_SHORT).show();
                        Log.i(TAG,"recyclerview item click: ");
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                        // send Content Uri with the id of the clicked item
                        intent.setData(ContentUris.withAppendedId(CountryContract.CountryEntry.CONTENT_URI,
                                position));
                        Log.w(TAG, "IN CLICK PASSED CONTENT URI: %%%%%%%%%%%%%%%%% " +
                                ContentUris.withAppendedId(CountryContract.CountryEntry.CONTENT_URI,
                                        position));
                        startActivity(intent);
                    }
                })
        );

        implementFloatingActionButtonClick();

        checkInternetPermission();

        getSupportLoaderManager().initLoader(LOADER_INIT, null, this);
    }

    private void checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){

            // permission is not granted :-> request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
            Log.i(TAG,"permission is not granted, requested");

        }{
            // permission granted
            Log.i(TAG, "permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                } else {
                    // permission denied,
                    // TODO display empty state view instead of map (image of map)
                    Toast.makeText(this, "Internet permission is required in order to display a map",
                            Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void implementFloatingActionButtonClick() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CountryContract.CountryEntry._ID,
                CountryContract.CountryEntry.COLUMN_COUNTRY_NAME,
                CountryContract.CountryEntry.COLUMN_VISITED_PERIOD
        };

        // This loader will execute ContentProvider's query method in a background thread
        return new CursorLoader(this, CountryContract.CountryEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(TAG, "onLoadFinished *************");
        myListCursorAdapter.swapCursor(cursor);
        //mCountriesRecyclerView.setAdapter(myListCursorAdapter);
        if(cursor != null){
//            mMyRecyclerAdapter = new MyRecyclerAdapter(this, cursor);
//            mCountriesRecyclerView.setAdapter(mMyRecyclerAdapter);
            mMapContent = "";

            while(cursor.moveToNext()){
                int mapContentIndex = cursor.getColumnIndexOrThrow(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME);
                String countryName = cursor.getString(mapContentIndex);
                generateMapContent(countryName);
            }
            setupWebView();
        }
        checkVisibility();
    }

    private void checkVisibility() {
        if(TextUtils.isEmpty(mMapContent)){
            mCountriesRecyclerView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mCountriesLabel.setVisibility(View.GONE);
            mLottieAnimationView.setVisibility(View.VISIBLE);
            mEmptyViewText.setVisibility(View.VISIBLE);
        } else{
            mCountriesRecyclerView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            mCountriesLabel.setVisibility(View.VISIBLE);
            mLottieAnimationView.setVisibility(View.GONE);
            mEmptyViewText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.i(TAG,"onLoadReset");
       // mMyRecyclerAdapter.swapCursor(null);
        myListCursorAdapter.swapCursor(null);
        mMapContent = "";
    }

    private void setupWebView() {

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.requestFocusFromTouch();
        mWebView.loadDataWithBaseURL("file:///android_asset/", mMapContent, "text/html", "utf-8", null);
    }

    private void generateMapContent(String countryName) {
        if (TextUtils.isEmpty(mMapContent)){
            mMapContent = "<html>"
                    + "  <head>"
                    + "    <script type=\"text/javascript\" src=\"loader.js\"></script>"
                    + "    <script type=\"text/javascript\">"
                    + "      google.charts.load(\"upcoming\", {packages:[\"geochart\"]});"
                    + "      google.charts.setOnLoadCallback(drawRegionsMap);"
                    + "      function drawRegionsMap() {"
                    + "        var data = google.visualization.arrayToDataTable(["
                    + "          ['Country', 'Value'],"
                    + "        ]);"
                    + "        var options = {"
                    + "          colorAxis: {values: [0, 1], "
                    + "colors: ['green', 'green']}," +
                    "backgroundColor: '#81d4fa',"+
                    "defaultColor: '#f5f5f5'," +
                    "datalessRegionColor: 'white'"
                    + "        };"
                    + "        var chart = new google.visualization.GeoChart(document.getElementById('geochart-colors'));"
                    + "        chart.draw(data, options);"
                    + "      }"
                    + "    </script>"
                    + "  </head>"
                    + "  <body>"
                    + "    <div id=\"geochart-colors\" style=\"width: 1000px; height: 600px;\"></div>"
                    + "  </body>" + "</html>";
        }
        mMapContent = mMapContent.substring(0,350) +  "['" + countryName + "'," + "0" + "]," +
                mMapContent.substring(350,mMapContent.length());
        Log.i(TAG, "map content " + mMapContent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MAP_CONTENT", mMapContent);
        Log.i(TAG, "onSaveInstanceState");
    }
}
