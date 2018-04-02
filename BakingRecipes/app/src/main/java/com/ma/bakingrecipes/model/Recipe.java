package com.ma.bakingrecipes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by amatanat.
 */

@Entity(tableName = "recipe")
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;


    public Recipe(int id, String name, Integer servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    @Ignore
    public Recipe(String name, Integer servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Integer getId() {
        return id;
    }

    @Ignore
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Ignore
    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Ignore
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    @Ignore
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    @Ignore
    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    @Ignore
    public void setImage(String image) {
        this.image = image;
    }
}
