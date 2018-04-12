package com.ma.bakingrecipes.ui.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ma.bakingrecipes.R;
import com.ma.bakingrecipes.model.Ingredient;

import java.util.List;


public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private Integer mValues;

    public MyItemRecyclerViewAdapter() {
        mValues = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String text;
        if(position == 0){
            text = "Ingredients Description";
        } else{
            text = "Recipe Step " + position + " Description";
        }
        holder.mItem.setText(text);
    }

    public void swapValue(Integer value){
        mValues = value;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mItem;

        public ViewHolder(View view) {
            super(view);
            mItem = view.findViewById(R.id.cv_textview);

        }

    }
}
