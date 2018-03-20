package de.amatanat.movie.data.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by amatanat.
 */

public class MovieContract {

    // Authority name for the MovieProvider
    public static final String CONTENT_AUTHORITY = "de.am.popular";

    /*
     Content Uri without data type
     * Parse string into URI
      */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // table name for the MovieProvider Content URI
    public static final String PATH_MOVIES = "movies";

    private MovieContract() {
    }

    public static final class MovieEntry implements BaseColumns{
        // Content URI with the table name. Appends string table name to the base URI
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

        public static final String TABLE_NAME = "movies";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_MOVIE_NAME = "name";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_RATING = "rating";

        public static final String COLUMN_MOVIE_DATE = "date";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_PICTURE = "picture";


        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
    }
}
