package com.ma.traveldroid.ui.statistics;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.ma.traveldroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsActivity extends AppCompatActivity {

    public static final String TAG = StatisticsActivity.class.getName();
    public static final String EXTRA_COUNT = "extra_count";

    private int mCount;

    @BindView(R.id.visited_countries)
    TextView mVisitedCountries;
    @BindView(R.id.percentage)
    TextView mPercentage;
    @BindView(R.id.pie_chart_wv)
    WebView mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_COUNT)){
            mCount = intent.getIntExtra(EXTRA_COUNT,0);
            Log.i(TAG,"countries count " + mCount);
        }
        updateUI();
    }

    private void updateUI() {
        StringBuilder stringBuilder = new StringBuilder(" ");
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
    }
}
