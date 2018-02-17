package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
        TextView originLabel = findViewById(R.id.origin_label);
        checkVisibilityAndSetText(sandwich.getPlaceOfOrigin(), originTv, originLabel);

        TextView alsoKnowTv = findViewById(R.id.also_known_tv);
        TextView alsoKnowLabel = findViewById(R.id.also_known_label);
        List<String> alsoKnowArray = sandwich.getAlsoKnownAs();
        String alsoKnownText = join(alsoKnowArray);
        checkVisibilityAndSetText(alsoKnownText, alsoKnowTv, alsoKnowLabel);

        TextView descriptionTv = findViewById(R.id.description_tv);
        TextView descriptionLabel = findViewById(R.id.description_label);
        checkVisibilityAndSetText(sandwich.getDescription(), descriptionTv, descriptionLabel);

        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        TextView ingredientsLabel = findViewById(R.id.ingredients_label);
        List<String> ingredientsArray = sandwich.getIngredients();
        String ingredientsText = join(ingredientsArray);
        checkVisibilityAndSetText(ingredientsText, ingredientsTv, ingredientsLabel);
    }

    /***
     * This method checks input text, if it's empty then sets visibility of textview and corresponding textviewlabel to be GONE
     *  otherwise sets to be VISIBLE and sets the textview text.
     * @param text input text for textview
     */
    private void checkVisibilityAndSetText(String text, TextView textView, TextView textViewLabel){
        if(TextUtils.isEmpty(text)){
            textView.setVisibility(View.GONE);
            textViewLabel.setVisibility(View.GONE);
        } else{
            textView.setVisibility(View.VISIBLE);
            textViewLabel.setVisibility(View.VISIBLE);
            textView.setText(text);
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

    /**
     * This method converts string list to string and adds separator.
     * Another way: TextUtils.join(CharSequence delimiter, Iterable tokens)
     * example TextUtils.join(", ", ingredientsArray)
     */
    public static String join(List<String> input) {

        StringBuilder stringBuilder = new StringBuilder();

        String separator = "";

        for(String s : input) {

            stringBuilder.append(separator);
            stringBuilder.append(s);

            separator = ", ";
        }

        return stringBuilder.toString();
    }
}
