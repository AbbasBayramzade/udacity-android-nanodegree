package com.ma.traveldroid.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.ma.traveldroid.roomDb.CountryDatabase;
import com.ma.traveldroid.roomDb.CountryEntry;

public class DetailViewModel extends ViewModel {

    private LiveData<CountryEntry> country;

    public DetailViewModel(CountryDatabase countryDatabase, int countryId) {
        country = countryDatabase.countryDao().getCountryById(countryId);
    }

    public LiveData<CountryEntry> getCountry(){
        return country;
    }
}
