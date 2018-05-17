package com.ma.traveldroid.ui.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ma.traveldroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsActivity extends AppCompatActivity {

    public static final String TAG = StatisticsActivity.class.getName();
    public static final String EXTRA_COUNT = "extra_count";

    private int mCount;
    private String mChartContent;

    @BindView(R.id.visited_countries)
    TextView mVisitedCountries;
    @BindView(R.id.percentage)
    TextView mPercentage;
    @BindView(R.id.pie_chart_wv)
    WebView mPieChart;
    @BindView(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);

        ButterKnife.bind(this);

        if(savedInstanceState != null)
            mCount = savedInstanceState.getInt(EXTRA_COUNT);

        mChartContent = "";
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_COUNT)){
            mCount = intent.getIntExtra(EXTRA_COUNT,0);
            Log.i(TAG,"countries count " + mCount);
        }
        updateUI();

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    private void updateUI() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Visited");
        stringBuilder.append(" ");
        stringBuilder.append(mCount);
        mVisitedCountries.setText(stringBuilder.toString());

        stringBuilder = new StringBuilder();
        if(mCount == 0)
            mPercentage.setText("0%");
        else{
            float percent = (mCount * 100.0f) / 195;
            String s = String.format("%.2f", percent);
            Log.i(TAG, "percentage " + percent);
            stringBuilder.append(s);
            stringBuilder.append("%");
            mPercentage.setText(stringBuilder.toString());
        }

        generateChartContent();
        setupWebView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_COUNT, mCount);
    }

    private void generateChartContent() {
        mChartContent = "<html>"
                + "  <head>"
                + "    <script type=\"text/javascript\" src=\"loader.js\"></script>"
                + "    <script type=\"text/javascript\">"
                + "      google.charts.load(\"current\", {packages:[\"corechart\"]});"
                + "      google.charts.setOnLoadCallback(drawChart);"
                + "      function drawChart() {"
                + "        var data = google.visualization.arrayToDataTable(["
                + "          ['Visited', 'Count'],"
                + "        ]);"
                + "        var options = {"
                + "          title: 'Statistics', "
                + "          is3D: true, "
                + "          backgroundColor: '#e1bee7',"
                + "        };"
                + "        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));"
                + "        chart.draw(data, options);"
                + "      }"
                + "    </script>"
                + "  </head>"
                + "  <body>"
                + "    <div id=\"piechart_3d\"style=\"width: 1000px; height: 600px;\"></div>"
                + "  </body>" + "</html>";

        mChartContent = mChartContent.substring(0, 344)
                + "['" + "Visited" + "'," + mCount + "]," +
                "['" + "Remains" + "'," + (195 - mCount) + "]," +
                mChartContent.substring(344, mChartContent.length());
        Log.i(TAG, "map content " + mChartContent);

    }

    private void setupWebView() {

        WebSettings webSettings = mPieChart.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mPieChart.getSettings().setLoadWithOverviewMode(true);
        mPieChart.getSettings().setUseWideViewPort(true);
        mPieChart.requestFocusFromTouch();
        mPieChart.loadDataWithBaseURL("file:///android_asset/", mChartContent, "text/html", "utf-8", null);
    }
}
