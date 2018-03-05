package de.amatanat.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.amatanat.popularmovies.R;
import de.amatanat.popularmovies.data.database.MovieEntry;
import de.amatanat.popularmovies.ui.detail.DetailActivity;

public class MainActivity extends AppCompatActivity {

    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<MovieEntry> movieEntries = new ArrayList<>();

        GridView gridView = findViewById(R.id.gridview);
        adapter = new CustomAdapter(this, movieEntries);
        gridView.setAdapter(adapter);

//        MainViewModelFactory viewModelFactory = InjectorUtils
//                .provideMainActivityViewModelFactory(this.getApplicationContext());
//        mViewModel = ViewModelProviders
//                .of(this, viewModelFactory)
//                .get(MainActivityViewModel.class);
//        mViewModel.getCurrentWeatherForecasts().observe(this, weatherEntries ->{
//            mForecastAdapter.swapForecast(weatherEntries);
//            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
//            mRecyclerView.smoothScrollToPosition(mPosition);
//
//            if (weatherEntries != null && weatherEntries.size() != 0) showWeatherDataView();
//            else showLoading();
//        });


//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(MainActivity.this, "" + position,
//                        Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, DetailActivity.class));
//            }
//        });

    }
}
