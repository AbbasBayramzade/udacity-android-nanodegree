package de.amatanat.movie.ui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.amatanat.movie.R;
import de.amatanat.movie.adapters.CustomAdapter;
import de.amatanat.movie.adapters.MovieCursorAdapter;
import de.amatanat.movie.data.Movie;
import de.amatanat.movie.data.db.MovieContract;
import de.amatanat.movie.utilities.loaders.MovieLoader;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks {

    private static final String TAG = MainActivity.class.getName();
    public static final String KEY_MOVIE_DETAIL = "movie_detail";

    private CustomAdapter adapter;
    private MovieCursorAdapter movieCursorAdapter;
    private ProgressBar progressBar;
    private TextView emptyStateText;
    private GridView gridView;

    private SharedPreferences sharedpreferences;
    public static final String PREFERENCE = "pref";
    public static final String KEY_ID = "id";

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String ENDPOINT_POPULAR = "popular?api_key=";
    private static final String ENDPOINT_TOP_RATED = "top_rated?api_key=";

    // TODO ADD YOUR API KEY HERE
    private static final String API_KEY = "";

    private static final String POPULAR_MOVIE_URL = BASE_URL + ENDPOINT_POPULAR + API_KEY;
    private static final String TOP_RATED_MOVIE_URL = BASE_URL + ENDPOINT_TOP_RATED + API_KEY;

    // unique idS for the loader
    private final int UNIQUE_ID_POPULAR = 1;
    private final int UNIQUE_ID_TOP_RATED = 2;

    private final int UNIQUE_ID_FAVORITES = 3;

    private LoaderManager loaderManager;

    private List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridview);
        progressBar = findViewById(R.id.loading_spinner);
        emptyStateText = findViewById(android.R.id.empty);

        adapter = new CustomAdapter(this, movies);
        movieCursorAdapter = new MovieCursorAdapter(this, null);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                sharedpreferences = getSharedPreferences(PREFERENCE,
                        Context.MODE_PRIVATE);
                if (sharedpreferences.contains(KEY_ID)) {
                    int ID = sharedpreferences.getInt(KEY_ID, UNIQUE_ID_POPULAR);

                    // if gridview doesn't display favorites list
                    if(ID != UNIQUE_ID_FAVORITES){
                        // on gridview item click pass clicked Movie to DetailActivity and start activity
                        launchDetailActivity(position);

                        Log.v(TAG,"pass movie detail");
                    } else{
                        // gridview displays 'favorites' list
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                        Log.v(TAG,"pass content uri");
                        // send Content Uri with the id of the clicked item
                        intent.setData(ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id));
                        startActivity(intent);
                    }
                } else if (!movies.isEmpty()){
                    // if movies list isn't empty, i.e gridview displays movies by popularity or rating
                    launchDetailActivity(position);

                    Log.v(TAG,"pass movie detail");
                } else if (movies.isEmpty()){

                    // gridview displays 'favorites' list
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Log.v(TAG,"pass content uri");
                    // send Content Uri with the id of the clicked item
                    intent.setData(ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id));
                    startActivity(intent);
                }
            }
        });

        //check network connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {

            // if device connected to the network

            // Get a reference to the LoaderManager, in order to interact with loaders.
            loaderManager = getLoaderManager();
            sharedpreferences = getSharedPreferences(PREFERENCE,
                    Context.MODE_PRIVATE);
            if (sharedpreferences.contains(KEY_ID)) {
                int id = sharedpreferences.getInt(KEY_ID, UNIQUE_ID_POPULAR);
                Log.d(TAG, "shared pref id: " + id);

                // Initialize the loader. Pass in the int ID constant  and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(id, null, this);
            } else{
                Log.d(TAG, "shared pref has no id");
               loaderManager.initLoader(UNIQUE_ID_POPULAR, null, this);
            }



        } else {

            // change visibility of progress bar
            progressBar.setVisibility(View.GONE);

            // change visibility of textview to be 'VISIBLE'
            emptyStateText.setVisibility(View.VISIBLE);

            // set text of textview
            emptyStateText.setText(R.string.message_no_internet);

        }

    }

    private void launchDetailActivity(int clickedItemPosition) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_MOVIE_DETAIL, movies.get(clickedItemPosition));
        startActivity(intent);
    }

    private void saveSharedPreference(int unique_id) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(KEY_ID, unique_id);
        editor.apply();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle bundle) {
        if(id == UNIQUE_ID_POPULAR)
            return new MovieLoader(this, POPULAR_MOVIE_URL);
        else if(id == UNIQUE_ID_TOP_RATED)
            return new MovieLoader(this, TOP_RATED_MOVIE_URL);
        else {
            String[] projection = {
                    MovieContract.MovieEntry._ID,
                    MovieContract.MovieEntry.COLUMN_MOVIE_PICTURE
            };

            // This loader will execute ContentProvider's query method in a background thread
            return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null);
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        int id = loader.getId();

        if(id == UNIQUE_ID_FAVORITES){
            Log.d(TAG, "load finished");

            // change visibility of progress bar
            progressBar.setVisibility(View.GONE);

            movieCursorAdapter.swapCursor((Cursor) o);
            gridView.setAdapter(movieCursorAdapter);


           checkVisibility(movieCursorAdapter.getCount());

        } else{
            Log.d(TAG, "load finished");

            // change visibility of progress bar
            progressBar.setVisibility(View.GONE);

            // clear the adapter
            adapter = new CustomAdapter(this, new ArrayList<Movie>());
            if(!((List<Movie>) o).isEmpty()){

                List<Movie> data = (List<Movie>) o;
                movies = data;
                Log.d(TAG, "result is not empty");
                adapter = new CustomAdapter(this, movies);
                gridView.setAdapter(adapter);

                checkVisibility(adapter.getItemCount());
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();

        if(id == UNIQUE_ID_FAVORITES){
            gridView.setAdapter(movieCursorAdapter);
            movieCursorAdapter.swapCursor(null);

        } else{
            gridView.setAdapter(adapter);
            adapter = new CustomAdapter(this, new ArrayList<Movie>());
        }
    }

    private void checkVisibility(int size) {

        // if is empty
        if (size == 0) {

            // change visibility of gridview to be 'GONE'
            gridView.setVisibility(View.GONE);

            // change visibility of textview to be 'VISIBLE'
            emptyStateText.setVisibility(View.VISIBLE);

            // set text of textview
            emptyStateText.setText(R.string.message_movie_list_empty);

        } else {
            // change visibility of gridview to be 'VISIBLE'
            gridView.setVisibility(View.VISIBLE);

            // change visibility of textview to be 'GONE'
            emptyStateText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                saveSharedPreference(UNIQUE_ID_POPULAR);
                loaderManager.initLoader(UNIQUE_ID_POPULAR, null, this);
                return true;
            case R.id.top_rated:
                saveSharedPreference(UNIQUE_ID_TOP_RATED);
                loaderManager.initLoader(UNIQUE_ID_TOP_RATED, null, this);
                return true;
            case R.id.favorites:
                saveSharedPreference(UNIQUE_ID_FAVORITES);
                loaderManager.initLoader(UNIQUE_ID_FAVORITES, null, this);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
