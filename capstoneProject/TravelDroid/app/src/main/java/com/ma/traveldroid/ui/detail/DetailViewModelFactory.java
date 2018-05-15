package com.ma.traveldroid.ui.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.ma.traveldroid.roomDb.CountryDatabase;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CountryDatabase mDatabase;
    private final int mCountryId;

    public DetailViewModelFactory(CountryDatabase countryDatabase, int countryId){
        mDatabase  = countryDatabase;
        mCountryId = countryId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mDatabase, mCountryId);
    }
}
