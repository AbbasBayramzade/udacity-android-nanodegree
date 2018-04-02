package com.ma.bakingrecipes.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.utilities.InjectorUtils;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = DetailActivity.class.getName();

    private DetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // for testing purposes just pass the name
        DetailViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(this, "Nutella Pie");

        mViewModel = ViewModelProviders.of(this, factory)
                .get(DetailActivityViewModel.class);
        mViewModel.getRecipe().observe(this, value -> {
            if (value != null){
                Log.d(TAG, value.getName());
                Log.d(TAG,value.getImage());
                //bindRecipeToUI(value);
            }
        });
    }
}
