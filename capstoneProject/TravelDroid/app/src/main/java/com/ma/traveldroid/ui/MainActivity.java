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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ma.traveldroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 100;

    @BindView(R.id.countries_recyclerview)
    RecyclerView mCountriesRecyclerView;
    @BindView(R.id.webview)
    WebView mWebView;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        implementFloatingActionButtonClick();
        
        checkInternetPermission();
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
            // permission granted :-> perform operation.
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

                    // TODO get map content data from the database and display it
                } else {
                    // permission denied,
                    // TODO display toast message and empty state view instead of map (image of map)
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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

    private void setupWebView(String text) {
        //TODO get content from the db.
        String content = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load(\"upcoming\", {packages:[\"geochart\"]});"
                + "      google.charts.setOnLoadCallback(drawRegionsMap);"
                + "      function drawRegionsMap() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Country', 'Value'],"
                + "          ['Azerbaijan',  0],"
                + "          ['Georgia',  0],"
                + "          ['Iran',  0],"
                + "          ['Germany',  0],"
                + "          ['Canada', 0],"
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

        content = content.substring(0, 350) + "['" + text + "'," + "0" + "]," +
                content.substring(350, content.length());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.requestFocusFromTouch();
        mWebView.loadDataWithBaseURL("file:///android_asset/", content, "text/html", "utf-8", null);

        Log.d(TAG, content.substring(0, 350));
        Log.d(TAG, content);
    }
}
