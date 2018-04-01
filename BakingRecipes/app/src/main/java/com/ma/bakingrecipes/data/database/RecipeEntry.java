package com.ma.bakingrecipes.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by amatanat.
 */

@Entity(tableName = "recipe") //TODO name should be unique
public class RecipeEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private Integer servings;
    private String image;
    // TODO create type converter for the ingredients and the steps. OR use foreign key principle
    // TODO https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    // TODO https://developer.android.com/reference/android/arch/persistence/room/Relation.html
}
