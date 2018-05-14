package com.ma.traveldroid.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ma.traveldroid.roomDb.CountryDatabase;
import com.ma.traveldroid.roomDb.CountryEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<CountryEntry>> countries;

    public MainViewModel(@NonNull Application application) {
        super(application);
        CountryDatabase database = CountryDatabase.getInstance(this.getApplication());
        countries = database.countryDao().loadAllCountries();
    }

    public LiveData<List<CountryEntry>> getCountries(){
        return countries;
    }
}
