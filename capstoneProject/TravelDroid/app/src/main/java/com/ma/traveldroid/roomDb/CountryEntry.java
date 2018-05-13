package com.ma.traveldroid.roomDb;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "country")
public class CountryEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "country_name")
    private String countryName;
    @ColumnInfo(name = "visited_period")
    private String visitedPeriod;


    public CountryEntry(int id, String countryName, String visitedPeriod) {
        this.id = id;
        this.countryName = countryName;
        this.visitedPeriod = visitedPeriod;
    }

    @Ignore
    public CountryEntry(String countryName, String visitedPeriod) {
        this.countryName = countryName;
        this.visitedPeriod = visitedPeriod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getVisitedPeriod() {
        return visitedPeriod;
    }

    public void setVisitedPeriod(String visitedPeriod) {
        this.visitedPeriod = visitedPeriod;
    }
}
