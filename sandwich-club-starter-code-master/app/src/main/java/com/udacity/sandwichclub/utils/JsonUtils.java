package com.udacity.sandwichclub.utils;

import android.text.TextUtils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String KEY_NAME = "name";
    private static final String KEY_MAIN_NAME = "mainName";
    private static final String KEY_ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_INGREDIENTS =  "ingredients";

    public static Sandwich parseSandwichJson(String json) {

        if(TextUtils.isEmpty(json))
            return null;

        try {
            // get jsonobject from input string
            JSONObject jsonObject = new JSONObject(json);

            // get 'mainName'string content from the jsonobject 'name'
            String mainName = jsonObject.optJSONObject(KEY_NAME).optString(KEY_MAIN_NAME);

            // get "alsoKnownAs" list
            List<String> alsoKnownAs = getListFromJsonArray(jsonObject.optJSONObject(KEY_NAME).
                    optJSONArray(KEY_ALSO_KNOWN_AS));

            // get 'placeOfOrigin' string
            String placeOfOrigin = jsonObject.optString(KEY_PLACE_OF_ORIGIN);

            //get 'description' string
            String description = jsonObject.optString(KEY_DESCRIPTION);

            // get 'image' string
            String image = jsonObject.optString(KEY_IMAGE);

            // get ingredients list
            List<String> ingredients = getListFromJsonArray(jsonObject.optJSONArray(KEY_INGREDIENTS));

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * This method gets JSONArray as input and returns String list
     */
    private static List<String> getListFromJsonArray(JSONArray jsonArray){
        List<String> result = new ArrayList<>();
        // check if array isn't empty
        if (jsonArray.length() > 0){
            for (int i = 0; i < jsonArray.length(); i++)
                result.add(jsonArray.optString(i));
        }
        return result;
    }
}
