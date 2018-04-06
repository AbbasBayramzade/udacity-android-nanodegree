package com.ma.bakingrecipes.data.database;

/**
 * Created by amatanat.
 */

public class RecyclerViewRecipeEntry {
    private Integer id;
    private String name;
    private String image;

    public RecyclerViewRecipeEntry(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
