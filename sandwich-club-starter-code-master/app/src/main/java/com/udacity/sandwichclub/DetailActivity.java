package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView originTv = findViewById(R.id.origin_tv);
        originTv.setText(sandwich.getPlaceOfOrigin());

        // todo empty strings' visibility


        TextView alsoKnowTv = findViewById(R.id.also_known_tv);
        TextView alsoKnowLabel = findViewById(R.id.also_known_label);
        String[] alsoKnowArray = sandwich.getAlsoKnownAs().toArray(new String[0]);
        if (alsoKnowArray.length == 0){
            alsoKnowTv.setVisibility(View.GONE);
            alsoKnowLabel.setVisibility(View.GONE);
        }else{
            alsoKnowTv.setVisibility(View.VISIBLE);
            alsoKnowLabel.setVisibility(View.VISIBLE);
            alsoKnowTv.setText(Arrays.toString(alsoKnowArray));
        }

        TextView descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(sandwich.getDescription());

        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        TextView ingredientsLabel = findViewById(R.id.ingredients_label);
        // get ingredients list and convert it to string array
        String[] ingredientsArray = sandwich.getIngredients().toArray(new String[0]);
        if(ingredientsArray.length == 0){
            ingredientsTv.setVisibility(View.GONE);
            ingredientsLabel.setVisibility(View.GONE);
        } else{
            ingredientsTv.setVisibility(View.VISIBLE);
            ingredientsLabel.setVisibility(View.VISIBLE);
            //todo set text
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
