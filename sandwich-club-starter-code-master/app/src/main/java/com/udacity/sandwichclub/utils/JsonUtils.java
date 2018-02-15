package com.udacity.sandwichclub.utils;

import android.text.TextUtils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        if(TextUtils.isEmpty(json))
            return null;

        try {
            // get jsonobject from input string
            JSONObject jsonObject = new JSONObject(json);

            // get 'mainName'string content from the jsonobject 'name'
            String mainName = jsonObject.getJSONObject("name").getString("mainName");

            // get jsonarray "alsoKnownAs"
            JSONArray jsonArray = jsonObject.getJSONObject("name").
                                    getJSONArray("alsoKnownAs");
            List<String> alsoKnownAs = new ArrayList<>();
            // check if array isn't empty
            if (jsonArray.length() > 0){
                for (int i = 0; i < jsonArray.length(); i++)
                    alsoKnownAs.add(jsonArray.getString(i));
            }

            // get 'placeOfOrigin' string
            String placeOfOrigin = jsonObject.getString("placeOfOrigin");
            //get 'description' string
            String description = jsonObject.getString("description");
            // get 'image' string
            String image = jsonObject.getString("image");

            // get jsonarray 'ingredients'
            JSONArray ingredientsJsonArray = jsonObject.getJSONArray("ingredients");
            List<String> ingredients = new ArrayList<>();
            if(ingredientsJsonArray.length() > 0){
                for (int i = 0; i < jsonArray.length(); i++)
                    ingredients.add(ingredientsJsonArray.getString(i));
            }

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
