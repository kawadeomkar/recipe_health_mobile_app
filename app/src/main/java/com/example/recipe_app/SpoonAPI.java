package com.example.recipe_app;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoonAPI  {
    private RequestQueue mQueue;
    private String url_searchRecipeByIngredient = "https://spoonacular-recipe-food-nutrition-v1" +
            ".p.rapidapi.com/recipes/findByIngredients";
    private String header_key = "vKls1ysqi6mshp5kJSbAidp6k9CVp14y8mFjsn0fKHab0671nS";


    /* given a recipe id, it  will return all information about that recipe (ingredients,
    nutritional breakdown, time etc.
    TODO: finish pojos for nutrition and then finish this api call
     */
    public RecipeFull getRecipeInformation(int id) {
        RecipeFull recipe = new RecipeFull();

        return recipe;
    }


    /* this function returns a list of random recipes depending on ingredients and a number
    url_query: adds on parameters
    num2: final version for inner onResponse method (needs final)
    result: returns a list of recipe objects
     */
    public List<RecipeTemp> getRandomRecipes(List<String> ingredients, int number) {
        final List<RecipeTemp> result = new ArrayList<>();
        String ranking = "2";
        final int num2 = number;
        String url_query = url_searchRecipeByIngredient + "?number=" + Integer.toString(number)
                + "&ranking=" + ranking +"&ingredients=";

        // add on ingredients to url string
        url_query += ingredients.get(0);
        for (int i = 0; i < ingredients.size()-1; ++i) {
            url_query += "%2C" + ingredients.get(i);
        }

        // make the json request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_query,
                null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                // convert response into json array, then loop through each object and store into
                // recipe object
                try {
                    JSONArray json_recipes = new JSONArray(response);
                        for (int j = 0; j < num2; ++j) {
                            JSONObject recipe_object = json_recipes.getJSONObject(j);
                            RecipeTemp recipe = new RecipeTemp();

                            recipe.setId(Integer.toString(recipe_object.getInt("id")));
                            recipe.setImage(recipe_object.getString("image"));
                            recipe.setMissedIngredientCount(recipe_object
                                    .getString("missedIngredientCount"));
                            recipe.setUsedIngredientCount(recipe_object
                                    .getString("usedIngredientCount"));
                            recipe.setTitle(recipe_object.getString("title"));

                            result.add(recipe);
                        }
                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("X-RapidAPI-Key", header_key);
                return headers;
            }
        };

        return result;
    }
}
