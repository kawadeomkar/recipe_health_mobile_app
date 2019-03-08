package com.example.recipe_app;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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
    private String url_searchRecipeByIngredient = "https://spoonacular-recipe-food-nutrition-v1." +
            "p.rapidapi.com/recipes/searchComplex";
    private String header_key = "vKls1ysqi6mshp5kJSbAidp6k9CVp14y8mFjsn0fKHab0671nS";
    private final String TAG = "TAG:";

    private List<RecipeTemp> recipeComplex;


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
    public List<RecipeTemp> getRandomRecipes(List<String> ingredients, int number, RequestQueue requestQueue) {
        final List<RecipeTemp> recipeComplex = new ArrayList<>();
        String ranking = "0";
        String offset = "0";
        String limitLicense = "false";
        String instructionsRequired = "true";
        // some parameters to add later
        String excludeIngredients;
        String intolerances;
        String diet; // vegan, keto etc
        String url_query = url_searchRecipeByIngredient + "?number=" + Integer.toString(number)
                + "&ranking=" + ranking + "&offset=" + offset + "&limitLicense=" + limitLicense +
                "&instructionsRequired=" + instructionsRequired + "&includeIngredients=";

        // add on ingredients to url string
        url_query += ingredients.get(0);
        for (int i = 0; i < ingredients.size()-1; ++i) {
            url_query += "%2C+" + ingredients.get(i);
        }
        // make the json request
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_query,
                null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                // convert response into json array, then loop through each object and store into
                // recipe object

                RecipeComplexHelper(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR_CHECKER", error.getMessage());
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

        requestQueue.add(req);
        Log.d(TAG, "THE FINAL COUNTDOWN " + recipeComplex);
        List<RecipeTemp> temp = new ArrayList<>(recipeComplex);
        recipeComplex.clear();
        return temp;
    }

    public void RecipeComplexHelper(JSONObject response) {
        recipeComplex = new ArrayList<>();
        try {
            JSONArray json_recipes = response.getJSONArray("results");

            for (int j = 0; j < json_recipes.length(); ++j) {
                JSONObject recipe_object = json_recipes.getJSONObject(j);
                RecipeTemp recipe = new RecipeTemp();

                recipe.setId(Integer.toString(recipe_object.getInt("id")));
                recipe.setImage(recipe_object.getString("image"));
                recipe.setMissedIngredientCount(recipe_object
                        .getString("missedIngredientCount"));
                recipe.setUsedIngredientCount(recipe_object
                        .getString("usedIngredientCount"));
                recipe.setTitle(recipe_object.getString("title"));

                            /* unsure how many recipes contain this, block off for now
                            recipe.setCalories(recipe_object.getString("calories"));
                            recipe.setCalories(recipe_object.getString("carbs"));
                            recipe.setCalories(recipe_object.getString("fat"));
                            recipe.setCalories(recipe_object.getString("protein"));
                            */

                recipeComplex.add(recipe);
                Log.d("ERROR_CHECKER: ", "Were adding the result now with size: " +
                        Integer.toString(recipeComplex.size()));
            }
            Log.d("ERROR_CHECKER", "Were returning the result now with size: " +
                    Integer.toString(recipeComplex.size()));

        } catch (Exception e) {
            Log.d("ERROR_CHECKER","Unfortunately, we've come across an error and that is: ");
            Log.d("ERROR_CHECKER", e.getMessage());
        }
    }
}
