package de.amatanat.popularmovies.utilities;

import android.content.Context;

import de.amatanat.popularmovies.data.MovieRepository;
import de.amatanat.popularmovies.ui.list.MainViewModelFactory;

/**
 * Created by amatanat.
 */

public class InjectorUtils {

//    public static MovieRepository provideRepository(Context context) {
//        MovieRepository database = MovieRepository.getInstance(context.getApplicationContext());
//        AppExecutors executors = AppExecutors.getInstance();
//        WeatherNetworkDataSource networkDataSource =
//                WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
//        return SunshineRepository.getInstance(database.weatherDao(), networkDataSource, executors);
//    }

//    public static WeatherNetworkDataSource provideNetworkDataSource(Context context) {
//        provideRepository(context.getApplicationContext());
//        AppExecutors executors = AppExecutors.getInstance();
//        return WeatherNetworkDataSource.getInstance(context.getApplicationContext(), executors);
//    }

//    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, Date date) {
//        SunshineRepository repository = provideRepository(context.getApplicationContext());
//        return new DetailViewModelFactory(repository, date);
//    }

//    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
//        MovieRepository repository = provideRepository(context.getApplicationContext());
//        return new MainViewModelFactory(repository);
//    }

}
