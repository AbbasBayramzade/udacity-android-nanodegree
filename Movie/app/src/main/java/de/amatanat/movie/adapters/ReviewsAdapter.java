package de.amatanat.movie.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.amatanat.movie.R;
import de.amatanat.movie.data.Model;

/**
 * Created by amatanat.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.CustomViewHolder> {

    private List<Model> reviews;

    public ReviewsAdapter(List<Model> reviews){
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout for custom adapter
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.content.setText(reviews.get(position).getContent());
        holder.name.setText(reviews.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView content;

        public CustomViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.reviewer_name_tv);
            content = itemView.findViewById(R.id.reviewer_text_tv);

        }
    }
}
