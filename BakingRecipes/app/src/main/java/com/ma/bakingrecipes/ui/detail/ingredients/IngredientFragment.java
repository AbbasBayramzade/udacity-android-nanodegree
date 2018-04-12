package com.ma.bakingrecipes.ui.detail.ingredients;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.ui.detail.SharedViewModel;
import com.ma.bakingrecipes.ui.detail.SharedViewModelFactory;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientFragment extends Fragment {

    private final String TAG = IngredientFragment.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";

    private SharedViewModel mViewModel;
    private IngredientItemRecyclerViewAdapter adapter;
    private String recipeName;

    @BindView(R.id.ingredients_recycler_view)
    RecyclerView recyclerView;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        ButterKnife.bind(this, view);

        if(savedInstanceState != null)
            recipeName = savedInstanceState.getString(KEY_RECIPE_NAME);
        else{
            if(getArguments() != null && getArguments().containsKey(KEY_RECIPE_NAME))
                recipeName = getArguments().getString(KEY_RECIPE_NAME);
        }

        Log.v(TAG, "recipe name: " + recipeName);

        adapter = new IngredientItemRecyclerViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        SharedViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(getContext(), recipeName);
        mViewModel = ViewModelProviders.of(getActivity(), factory)
                .get(SharedViewModel.class);

        mViewModel.getRecipe().observe(this, value -> {
            if (value != null)
                bindData(value);
        });

        return view;
    }

    private void bindData(Recipe value) {
       adapter.swapIngredients(value.getIngredients());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the fragment's state here
        outState.putString(KEY_RECIPE_NAME, recipeName);
        super.onSaveInstanceState(outState);

    }
}
