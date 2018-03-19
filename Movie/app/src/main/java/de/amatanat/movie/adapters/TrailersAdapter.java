package de.amatanat.movie.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.amatanat.movie.R;
import de.amatanat.movie.data.Model;

/**
 * Created by amatanat.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.CustomViewHolder>{

    private List<Model> trailers;
    private Context context;

    public TrailersAdapter(Context context, List<Model> trailers){
        this.trailers = trailers;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_list_item, parent, false);

        return new TrailersAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        String image = "http://img.youtube.com/vi/"
                .concat(trailers.get(position).getContent())
                .concat("/hqdefault.jpg");
        Picasso.with(context).
                load(image).
                into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.trailer_iv);
        }
    }
}
