package de.amatanat.movie.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.amatanat.movie.R;
import de.amatanat.movie.data.Review;

/**
 * Created by amatanat.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.CustomViewHolder> {

    private List<Review> reviews;

    public ReviewsAdapter(List<Review> reviews){
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
        holder.author.setText(reviews.get(position).getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView content;

        public CustomViewHolder(View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.reviewer_name_tv);
            content = itemView.findViewById(R.id.reviewer_text_tv);

        }
    }
}
