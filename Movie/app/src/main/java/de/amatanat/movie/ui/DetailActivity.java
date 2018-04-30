package de.amatanat.movie.ui;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.amatanat.movie.adapters.ReviewsAdapter;
import de.amatanat.movie.adapters.TrailersAdapter;
import de.amatanat.movie.R;
import de.amatanat.movie.data.Movie;
import de.amatanat.movie.data.Model;
import de.amatanat.movie.data.db.MovieContract;
import de.amatanat.movie.data.db.MovieDBHelper;
import de.amatanat.movie.utilities.loaders.ReviewLoader;
import de.amatanat.movie.utilities.loaders.TrailerLoader;

import static de.amatanat.movie.data.db.MovieContract.MovieEntry.TABLE_NAME;
import static de.amatanat.movie.ui.MainActivity.KEY_MOVIE_DETAIL;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private final String TAG = DetailActivity.class.getName();

    private final int LOAD_REVIEW = 500;
    private final int LOAD_TRAILER = 700;
    private final int LOAD_DATA = 900;

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String ENDPOINT_REVIEWS = "/reviews?api_key=";
    private static final String ENDPOINT_TRAILERS = "/videos?api_key=";

    // TODO ADD YOUR API KEY HERE
    private static final String API_KEY = "";

    private StringBuilder reviewURL, trailerURL;
    private LoaderManager loaderManager;
    private List<Model> reviews = new ArrayList<>();
    private List<Model> trailers = new ArrayList<>();
    private MovieDBHelper movieDBHelper;

    @BindView(R.id.favorite_bn) Button favorite;
    @BindView(R.id.movie_name_tv) TextView title;
    @BindView(R.id.movie_date_tv) TextView releaseDate;
    @BindView(R.id.movie_image_iv) ImageView image;
    @BindView(R.id.movie_rating_tv) TextView rating;
    @BindView(R.id.movie_overview_tv) TextView description;
    @BindView(R.id.reviews_rv) RecyclerView reviewRecyclerView;
    @BindView(R.id.review_label_tv) TextView reviewLabel;
    @BindView(R.id.trailers_rv) RecyclerView trailerRecyclerView;
    @BindView(R.id.trailer_label_tv) TextView trailerLabel;

    private ReviewsAdapter reviewsAdapter;
    private TrailersAdapter trailersAdapter;
    private Uri mContentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layout);

        ButterKnife.bind(DetailActivity.this);

        movieDBHelper = new MovieDBHelper(getApplicationContext());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        // set recycleview layoutmanager
        reviewRecyclerView.setLayoutManager(mLayoutManager);
        trailerRecyclerView.setLayoutManager(layoutManager);

        // set item animator in recycleview
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // implement recyclerview item touch
        trailerRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getApplicationContext(), trailerRecyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        String url = "https://www.youtube.com/watch?v="
                                .concat(trailers.get(position).getContent());
                        Log.d(TAG,"item click trailer url : "  + url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String url = "https://www.youtube.com/watch?v="
                                .concat(trailers.get(position).getContent());
                        Log.d(TAG,"long click trailer url : "  + url);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
        );

        reviewsAdapter = new ReviewsAdapter(reviews);
        trailersAdapter = new TrailersAdapter(this, trailers);


        // set adapter of recycleview
        reviewRecyclerView.setAdapter(reviewsAdapter);
        trailerRecyclerView.setAdapter(trailersAdapter);

        loaderManager = getLoaderManager();

        Bundle b = getIntent().getExtras();
        mContentUri = getIntent().getData();


        if (b != null && b.containsKey(KEY_MOVIE_DETAIL)) {
            // get passed parcelable
            final Movie movie = b.getParcelable(KEY_MOVIE_DETAIL);

            Log.v(TAG,"contains movie detail");
            // create review url

            loadReviewAndTrailer(movie);

            populateUI(movie);

            checkFavoriteButtonText(movie);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favoriteButtonClick(movie);
                }
            });


        } else if(mContentUri != null){
            Log.v(TAG,"contains content uri");
            loaderManager.initLoader(LOAD_DATA,null, this);
            favorite.setVisibility(View.GONE);
        }
    }

    private void loadReviewAndTrailer(Movie movie) {
        reviewURL = new StringBuilder();
        reviewURL.append(BASE_URL);
        reviewURL.append(movie.getId());
        reviewURL.append(ENDPOINT_REVIEWS);
        reviewURL.append(API_KEY);

        // create trailer url
        trailerURL = new StringBuilder();
        trailerURL.append(BASE_URL);
        trailerURL.append(movie.getId());
        trailerURL.append(ENDPOINT_TRAILERS);
        trailerURL.append(API_KEY);

        loaderManager.initLoader(LOAD_REVIEW, null, this);
        loaderManager.initLoader(LOAD_TRAILER, null, this);
    }

    private void favoriteButtonClick(Movie movie) {
        SQLiteDatabase db = movieDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE movie_id = '" +
                movie.getId() + "'", null);
        if (cursor.getCount() > 0) { // This will get the number of rows
            // It has data
            Log.i(TAG,"table contains this movie");
            favorite.setText(R.string.add_to_favorite);
            deleteDataFromDb(movie);
        } else {
            Log.i(TAG,"table does not contain this movie");
            insertDataIntoDb(movie);
            favorite.setText(R.string.text_remove_from_favorite);
        }
        cursor.close();
    }


    private void checkFavoriteButtonText(Movie movie) {

        SQLiteDatabase db = movieDBHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE movie_id = '" +
                movie.getId() + "'", null);
        if (cursor.getCount() > 0) { // This will get the number of rows
            // It has data
            Log.i(TAG,"table contains this movie");
            favorite.setText(R.string.text_remove_from_favorite);
        } else {
            Log.i(TAG,"table does not contain this movie");
            favorite.setText(R.string.add_to_favorite);
        }
        cursor.close();
    }

    private void deleteDataFromDb(Movie movie) {
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();
        database.execSQL(String.format("DELETE FROM %s WHERE %s = %d",
                MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie.getId()));
        database.close();

    }

    private void insertDataIntoDb(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, movie.getTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getDescription());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, movie.getRating());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE, movie.getImage());

        Uri resultUri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        Log.i(TAG, "Result uri in insert method......" + resultUri);
    }

    private void populateUI(Movie movie) {

        title.setText(movie.getTitle());
        Log.i(TAG,"TITLE  " +  movie.getTitle());

        Log.i(TAG,"RELEASE DATE  " +  movie.getReleaseDate());
        releaseDate.setText(movie.getReleaseDate());

        Log.i(TAG,"IMAGE  " +  movie.getImage());
        Picasso.with(this.getApplicationContext()).load(movie.getImage()).into(image);

        Log.i(TAG,"RATING  " +  movie.getRating());
        String text = movie.getRating() + "/10";
        rating.setText(text);

        Log.i(TAG,"DESC " +  movie.getDescription());
        description.setText(movie.getDescription());
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        if (id == LOAD_REVIEW)
            return new ReviewLoader(this, reviewURL.toString());
        else if (id == LOAD_TRAILER)
            return new TrailerLoader(this, trailerURL.toString());
        else{
            String[] projection = {
                   MovieContract.MovieEntry._ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_DATE,
                    MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                    MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
            };
            return new CursorLoader(this, mContentUri, projection, null, null, null);
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        int id = loader.getId();

        if(id == LOAD_REVIEW) {
            reviewsAdapter = new ReviewsAdapter(new ArrayList<Model>());

            if (!((List<Model>) o).isEmpty()) {

                List<Model> data = (List<Model>) o;
                reviews = data;
                Log.d(TAG, "result is not empty");
                reviewsAdapter = new ReviewsAdapter(reviews);
                reviewRecyclerView.setAdapter(reviewsAdapter);
                reviewRecyclerView.setVisibility(View.VISIBLE);
                reviewLabel.setVisibility(View.VISIBLE);
            } else {
                reviewRecyclerView.setVisibility(View.GONE);
                reviewLabel.setVisibility(View.GONE);
            }
        } else if (id == LOAD_TRAILER) {
            trailersAdapter = new TrailersAdapter(this, new ArrayList<Model>());

            if (!((List<Model>) o).isEmpty()) {
                List<Model> data = (List<Model>) o;
                trailers = data;
                Log.d(TAG, "result is not empty");
                trailersAdapter = new TrailersAdapter(this, trailers);
                trailerRecyclerView.setAdapter(trailersAdapter);
                trailerRecyclerView.setVisibility(View.VISIBLE);
                trailerLabel.setVisibility(View.VISIBLE);
            } else {
                trailerRecyclerView.setVisibility(View.GONE);
                trailerLabel.setVisibility(View.GONE);
            }
        }else if (id == LOAD_DATA) {
            if (((Cursor) o).moveToNext()) {
                Cursor cursor = (Cursor) o;
                int nameIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_NAME);
                int pictureIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE);
                int dateIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_DATE);
                int overviewIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
                int ratingIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
                int idIndex = cursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

                String nameText = cursor.getString(nameIndex);
                String pictureValue = cursor.getString(pictureIndex);
                String dateValue = cursor.getString(dateIndex);
                String overviewText = cursor.getString(overviewIndex);
                String rating = cursor.getString(ratingIndex);
                int movieId = cursor.getInt(idIndex);

                Movie movie = new Movie(movieId, nameText, pictureValue, dateValue, Double.parseDouble(rating), overviewText);
                populateUI(movie);
                loadReviewAndTrailer(movie);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();
        if (id == LOAD_DATA){
            title.setText("");
            releaseDate.setText("");
            rating.setText("");
            description.setText("");
        } else{
            reviewsAdapter = new ReviewsAdapter(new ArrayList<Model>());
            trailersAdapter = new TrailersAdapter(this, new ArrayList<Model>());
        }
    }
}
