package com.ma.traveldroid.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ma.traveldroid.R;
import com.ma.traveldroid.roomDb.CountryDatabase;
import com.ma.traveldroid.roomDb.CountryEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements CountryAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 100;
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
    private CountryAdapter mAdapter;
    private String mMapContent;
    private CountryDatabase mCountryDatabase;
    private List<CountryEntry> mCountryEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mLottieAnimationView.setAnimation("EmptyState.json");
        mLottieAnimationView.loop(true);
        mLottieAnimationView.playAnimation();

        if (savedInstanceState != null)
            mMapContent = savedInstanceState.getString("MAP_CONTENT");
        else
            mMapContent = "";

        mAdapter = new CountryAdapter(this, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mCountriesRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCountriesRecyclerView.addItemDecoration(decoration);

        mCountriesRecyclerView.setAdapter(mAdapter);

        mCountryDatabase = CountryDatabase.getInstance(getApplicationContext());


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<CountryEntry> countryEntries = mAdapter.getCountryEntries();
                        mCountryDatabase.countryDao().deleteCountry(countryEntries.get(position));
                        getCountries();
                    }
                });
            }
        }).attachToRecyclerView(mCountriesRecyclerView);


        implementFloatingActionButtonClick();

        checkInternetPermission();
    }

    private void checkInternetPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // permission is not granted :-> request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
            Log.i(TAG, "permission is not granted, requested");

        }
        {
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

    private void checkVisibility() {
        if (TextUtils.isEmpty(mMapContent)) {
            mCountriesRecyclerView.setVisibility(View.GONE);
            mWebView.setVisibility(View.GONE);
            mCountriesLabel.setVisibility(View.GONE);
            mLottieAnimationView.setVisibility(View.VISIBLE);
            mEmptyViewText.setVisibility(View.VISIBLE);
        } else {
            mCountriesRecyclerView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            mCountriesLabel.setVisibility(View.VISIBLE);
            mLottieAnimationView.setVisibility(View.GONE);
            mEmptyViewText.setVisibility(View.GONE);
        }
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
        if (TextUtils.isEmpty(mMapContent)) {
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
                    "backgroundColor: '#81d4fa'," +
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
        mMapContent = mMapContent.substring(0, 350) + "['" + countryName + "'," + "0" + "]," +
                mMapContent.substring(350, mMapContent.length());
        Log.i(TAG, "map content " + mMapContent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("MAP_CONTENT", mMapContent);
        Log.i(TAG, "onSaveInstanceState");
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCountries();
    }

    public void getCountries() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mCountryEntries = mCountryDatabase.countryDao().loadAllCountries();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setCountries(mCountryEntries);
                        mMapContent = "";
                        for (CountryEntry country : mCountryEntries) {
                            generateMapContent(country.getCountryName());
                        }

                        setupWebView();
                        checkVisibility();
                    }
                });
            }
        });
    }


    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, itemId);
        startActivity(intent);
    }
}
