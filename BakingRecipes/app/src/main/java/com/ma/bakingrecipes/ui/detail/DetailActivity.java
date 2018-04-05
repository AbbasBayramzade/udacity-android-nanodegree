package com.ma.bakingrecipes.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.ui.ingredients.IngredientsActivity;
import com.ma.bakingrecipes.ui.steps.StepDescriptionActivity;


public class DetailActivity extends AppCompatActivity implements ItemFragment.OnItemClickListener {

    private final String TAG = DetailActivity.class.getName();

    private final String KEY_POSITION = "CLICKED_POSITION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    }

    @Override
    public void onItemSelected(int position) {
        Intent intent;
        if(position == 0){
           intent = new Intent(DetailActivity.this, IngredientsActivity.class);
            Log.d(TAG, "OPEN ING");

        } else{
            intent = new Intent(DetailActivity.this, StepDescriptionActivity.class);
            Log.d(TAG, "OPEN STEPS");
        }
        intent.putExtra(KEY_POSITION, position);
        startActivity(intent);
    }
}
