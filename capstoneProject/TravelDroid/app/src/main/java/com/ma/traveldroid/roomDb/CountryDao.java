package com.ma.traveldroid.roomDb;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CountryDao {

    @Query("SELECT * FROM country ORDER BY country_name")
    LiveData<List<CountryEntry>> loadAllCountries();

    @Query("SELECT * FROM country WHERE id = :id")
    LiveData<CountryEntry> getCountryById(int id);

    @Insert
    void insertCountry(CountryEntry countryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCoutry(CountryEntry countryEntry);

    @Delete
    void deleteCountry(CountryEntry countryEntry);

    @Query("SELECT * FROM country WHERE country_name = :name")
    boolean dataExitsInDatabase(String name);
}
