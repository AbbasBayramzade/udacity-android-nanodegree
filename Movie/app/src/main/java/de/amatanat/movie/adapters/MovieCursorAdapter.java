package de.amatanat.movie.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.amatanat.movie.R;
import de.amatanat.movie.data.db.MovieContract;

/**
 * Created by amatanat.
 */

public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView movieImage = view.findViewById(R.id.imageView);
        String imageUri = cursor.getString
                (cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE));
//        Uri image = Uri.parse(imageUri);
//        movieImage.setImageURI(image);
        Picasso.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_placeholder_error)
                .error(R.drawable.ic_placeholder_error)
                .into(movieImage);

    }
}
