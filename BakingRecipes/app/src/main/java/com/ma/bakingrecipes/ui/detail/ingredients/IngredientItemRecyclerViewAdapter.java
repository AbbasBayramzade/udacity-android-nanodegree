package com.ma.bakingrecipes.ui.detail.ingredients;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amatanat.
 */

public class IngredientItemRecyclerViewAdapter extends RecyclerView.Adapter<IngredientItemRecyclerViewAdapter.ViewHolder> {

    private final String TAG = IngredientItemRecyclerViewAdapter.class.getSimpleName();
    private static final String SPACE= " ";
    private List<Ingredient> ingredients;

    public IngredientItemRecyclerViewAdapter() {
        ingredients = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ingredient.getQuantity()).append(SPACE);
        stringBuilder.append(ingredient.getMeasure()).append(SPACE);
        stringBuilder.append(ingredient.getIngredient()).append(SPACE);

        Log.d(TAG, "ingredient detail : " + stringBuilder.toString());
        holder.ingredientDetail.setText(stringBuilder.toString());
    }


    public void swapIngredients(List<Ingredient> ingredientList){
        ingredients = ingredientList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientDetail;

        public ViewHolder(View view) {
            super(view);
            ingredientDetail = view.findViewById(R.id.ingredient_detail);
        }

    }
}
