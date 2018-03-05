package de.amatanat.popularmovies.ui.list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.amatanat.popularmovies.R;
import de.amatanat.popularmovies.data.database.MovieEntry;

/**
 * Created by amatanat.
 */

public class CustomAdapter extends ArrayAdapter<MovieEntry> {

    private Context context;
    private List<MovieEntry> movieEntries;

    public CustomAdapter(Activity context, List<MovieEntry> movieEntries) {
        super(context, 0, movieEntries);
        this.context = context;
        this.movieEntries = movieEntries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieEntry movieEntry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        Picasso.with(context).load(movieEntry.getImage()).into(imageView);

//        TextView versionNameView = (TextView) convertView.findViewById(R.id.list_item_version_name);
//        versionNameView.setText(androidFlavor.versionName);

        return convertView;
    }


}
