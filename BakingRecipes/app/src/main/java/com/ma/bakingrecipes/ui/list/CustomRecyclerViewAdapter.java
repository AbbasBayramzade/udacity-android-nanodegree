package com.ma.bakingrecipes.ui.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.data.database.RecyclerViewRecipeEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amatanat.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.ViewHolder> {

    public interface ListItemClickListener {
        void onListItemClick(String clickedRecipeName);
    }

    private ListItemClickListener onClickListener;
    private List<RecyclerViewRecipeEntry> recipeEntries;
    private Context context;

    public CustomRecyclerViewAdapter(Context context, ListItemClickListener listener){
        this.context = context;
        onClickListener = listener;
        recipeEntries = new ArrayList<>();
    }

    @NonNull
    @Override
    public CustomRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomRecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewRecipeEntry recipeEntry = recipeEntries.get(position);
        holder.recipeName.setText(recipeEntry.getName());

        if(recipeEntry.getImage().isEmpty()){
            holder.recipeImage.setImageResource(R.drawable.ic_recipe_placeholer_image);
        }else{
            Picasso.with(context)
                    .load(recipeEntry.getImage())
                    .placeholder(R.drawable.ic_recipe_placeholer_image)
                    .error(R.drawable.ic_recipe_placeholer_image)
                    .into(holder.recipeImage);
        }

    }

    public void swapRecipes(List<RecyclerViewRecipeEntry> recipeEntries){
        this.recipeEntries = recipeEntries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return recipeEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView recipeImage;
        private TextView recipeName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            recipeImage = itemView.findViewById(R.id.recipe_icon);
            recipeName = itemView.findViewById(R.id.recipe_name);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onListItemClick(recipeEntries.get(getAdapterPosition()).getName());
        }
    }
}
