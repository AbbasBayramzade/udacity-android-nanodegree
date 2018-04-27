package com.ma.androidlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisplayJokeActivity extends AppCompatActivity {

    @BindView(R2.id.joke_text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_joke);

        ButterKnife.bind(this);

        if(getIntent().getExtras().containsKey("JOKE"))
            textView.setText(getIntent().getStringExtra("JOKE"));

    }
}
