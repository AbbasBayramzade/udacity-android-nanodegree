package com.ma.bakingrecipes.data.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ma.bakingrecipes.model.Ingredient;
import com.ma.bakingrecipes.model.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amatanat.
 */

class Convertors {

    @TypeConverter
    public static List<Ingredient> fromStringToIngredientList(String value) {
        Type listType = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromIngredientListToString(List<Ingredient> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static List<Step> fromStringToStepList(String value) {
        Type listType = new TypeToken<ArrayList<Step>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromStepListToString(List<Step> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
