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
import android.widget.TextView;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.model.Recipe;
import com.ma.bakingrecipes.ui.detail.DetailActivityViewModel;
import com.ma.bakingrecipes.ui.detail.DetailViewModelFactory;
import com.ma.bakingrecipes.ui.detail.MyItemRecyclerViewAdapter;
import com.ma.bakingrecipes.ui.detail.RecyclerItemClickListener;
import com.ma.bakingrecipes.utilities.InjectorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientFragment extends Fragment {

    private final String TAG = IngredientFragment.class.getName();
    private final String KEY_RECIPE_NAME = "recipe_name";

    private IngredientActivityViewModel mViewModel;
    private IngredientItemRecyclerViewAdapter adapter;

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

        adapter = new IngredientItemRecyclerViewAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        String recipeName = "";
        if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null &&
                getActivity().getIntent().getExtras().containsKey(KEY_RECIPE_NAME)){

            Log.d(TAG, "passed recipe name: " + getActivity().getIntent().getExtras().getString(KEY_RECIPE_NAME));
            recipeName = getActivity().getIntent().getExtras().getString(KEY_RECIPE_NAME);

        }

        IngredientViewModelFactory factory =
                InjectorUtils.provideIngredientViewModelFactory(getContext(), recipeName);

        mViewModel = ViewModelProviders.of(this, factory)
                .get(IngredientActivityViewModel.class);

        mViewModel.getRecipe().observe(this, value -> {
            if (value != null){
                Log.v(TAG, value.getName());
                Log.v(TAG,value.getIngredients().get(0).getIngredient());
                bindData(value);
            }
        });
        return view;
    }

    private void bindData(Recipe value) {
       adapter.swapIngredients(value.getIngredients());
    }

}
