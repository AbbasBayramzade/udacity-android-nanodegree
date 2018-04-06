package com.ma.bakingrecipes.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.ui.detail.DetailActivity;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CustomRecyclerViewAdapter.ListItemClickListener{

    private final String KEY_RECIPE_NAME = "recipe_name";
    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerview_recipe)
    RecyclerView recyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;

    private int position = RecyclerView.NO_POSITION;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(customRecyclerViewAdapter);
        showDataLoading();

        MainViewModelFactory factory = InjectorUtils
                .provideMainActivityViewModelFactory(this.getApplicationContext());
        viewModel = ViewModelProviders.of(this, factory)
                .get(MainActivityViewModel.class);
        viewModel.getRecipes().observe(this, recipeEntryList ->{
            customRecyclerViewAdapter.swapRecipes(recipeEntryList);
            if (position == RecyclerView.NO_POSITION) position = 0;
            recyclerView.smoothScrollToPosition(position);

            if (recipeEntryList != null && recipeEntryList.size() != 0) showRecipeDataView();
            else showDataLoading();
        });

    }

    private void showRecipeDataView() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showDataLoading() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(String clickedRecipeName) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_RECIPE_NAME, clickedRecipeName);
        Log.d(TAG, "clicked recipe name " + clickedRecipeName);
        startActivity(intent);
    }
}
