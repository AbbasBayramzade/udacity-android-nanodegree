package de.amatanat.movie.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.amatanat.movie.R;
import de.amatanat.movie.data.Movie;

/**
 * Created by amatanat.
 */

public class CustomAdapter extends ArrayAdapter<Movie> {

    private Context context;
    private List<Movie> movieList;

    public CustomAdapter(Activity context, List<Movie> movieList) {
        super(context, 0, movieList);
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        Picasso.with(context)
                .load(movie.getImage())
                .placeholder(R.drawable.ic_placeholder_error)
                .error(R.drawable.ic_placeholder_error)
                .into(imageView);

        return convertView;
    }

    public int getItemCount(){
        return movieList.size();
    }

}
