package de.amatanat.movie.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.amatanat.movie.R;

/**
 * Created by amatanat.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.CustomViewHolder> {

    private List<String> reviews;

    public ReviewsAdapter(List<String> reviews){
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
        holder.review.setText(reviews.get(position));
        holder.reviewerName.setText(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView reviewerName;
        private TextView review;

        public CustomViewHolder(View itemView) {
            super(itemView);

            reviewerName = itemView.findViewById(R.id.reviewer_name_tv);
            review = itemView.findViewById(R.id.reviewer_text_tv);

        }
    }
}
