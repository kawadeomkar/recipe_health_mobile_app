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
    private String url_complexRecipe = "https://spoonacular-recipe-food-nutrition-v1." +
            "p.rapidapi.com/recipes/searchComplex";
    private String header_key = "vKls1ysqi6mshp5kJSbAidp6k9CVp14y8mFjsn0fKHab0671nS";
    private String url_getRecipeWithID1 = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi." +
            "com/recipes/";
    private String url_getRecipeWithID2 = "/information?includeNutrition=true";

    private List<RecipeTemp> recipeComplex;
    private RecipeFull recipeIDFull;
    private boolean recipeIDFinished = false;
    private boolean recipeComplexFinished = false;

    // force synchronicity (refactor later)
    private void setRecipeIDBoolean() {
        recipeIDFinished = true;
    }

    // force synchronicity (refactor later)
    private void setRecipeComplexBoolean() {
        recipeComplexFinished = true;
    }

    // temp testing
    public RecipeFull getRecipeFull() {
        return recipeIDFull;
    }

    //
    public boolean getRecipeIDCheck() {
        return recipeIDFinished;
    }

    public void apiHelper(String url, String name, RequestQueue requestQueue) {
        Log.d("SPOONAPI", "ENTERED APIHELPER FUNCTION");
        final String call = name;
        Log.d("MAINACTIVITY", "ABOUT TO CREATE API REQUEST");
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                // convert response into json array, then loop through each object and store into
                // recipe object
                switch (call) {
                    case "recipeComplex":
                        getRecipeComplexHelper(response);
                        setRecipeComplexBoolean();
                    case "recipeID":
                        Log.d("SPOONAPI", "SANITY CHECK: CORRECT CASE");
                        getRecipeByIDHelper(response);
                        setRecipeIDBoolean();
                }

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
    }


    /* given a recipe id, it  will return all information about that recipe (ingredients,
    nutritional breakdown, time etc.
     */
    public void getRecipeByID(int id, RequestQueue requestQueue) {
        recipeIDFinished = false;
        Log.d("SPOONAPI", "ENTERED GETRECIPEBYID FUNCTION");
        String url_query = url_getRecipeWithID1 + Integer.toString(id) + url_getRecipeWithID2;
        recipeIDFull = new RecipeFull();
        Log.d("SPOONAPI", "ABOUT TO CALL API HELPER");
        apiHelper(url_query, "recipeID", requestQueue);
    }


    /* this function returns a list of recipes IDs depending on ingredients and a number
    url_query: adds on parameters
    num2: final version for inner onResponse method (needs final)
    result: returns a list of recipe objects
     */
    public List<RecipeTemp> getRecipeComplex(List<String> ingredients, int number, RequestQueue requestQueue) {
        recipeComplex = new ArrayList<>();
        String ranking = "0";
        String offset = "0";
        String limitLicense = "false";
        String instructionsRequired = "true";
        // some parameters to add later
        String excludeIngredients;
        String intolerances;
        String diet; // vegan, keto etc
        String url_query = url_complexRecipe + "?number=" + Integer.toString(number)
                + "&ranking=" + ranking + "&offset=" + offset + "&limitLicense=" + limitLicense +
                "&instructionsRequired=" + instructionsRequired + "&includeIngredients=";

        // add on ingredients to url string
        url_query += ingredients.get(0);
        for (int i = 0; i < ingredients.size()-1; ++i) {
            url_query += "%2C+" + ingredients.get(i);
        }
        // make the json request

        apiHelper(url_query,"recipeComplex", requestQueue);
        while (!recipeComplexFinished) {} // force synchronicity (sorry refactor later)
        recipeComplexFinished = false;
        return recipeComplex;
    }

    // helper function to set up recipe object for getRecipeComplex function
    public void getRecipeComplexHelper(JSONObject response) {
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

    // helper function for getting recipe information by ID
    public void getRecipeByIDHelper(JSONObject response) {
        Log.d("SPOONAPI", "SANITY CHECK: CORRECT RECIPEID HELPER FUNCTION");
        RecipeFull recipeFull = new RecipeFull();
        try {
            if (response.has("vegetarian")) {
                recipeFull.setVegetarian(response.getBoolean("vegetarian"));
            }
            if (response.has("vegan")) {
                recipeFull.setVegan(response.getBoolean("vegan"));
            }
            if (response.has("glutenFree")) {
                recipeFull.setGlutenFree(response.getBoolean("glutenFree"));
            }
            if (response.has("dairyFree")) {
                recipeFull.setDairyFree(response.getBoolean("dairyFree"));
            }
            if (response.has("veryHealthy")) {
                recipeFull.setVeryHealthy(response.getBoolean("veryHealthy"));
            }
            if (response.has("veryPopular")) {
                recipeFull.setVeryHealthy(response.getBoolean("veryPopular"));
            }
            if (response.has("lowFodMap")) {
                recipeFull.setLowFodmap(response.getBoolean("lowFodMap"));
            }
            if (response.has("ketogenic")) {
                recipeFull.setKetogenic(response.getBoolean("ketogenic"));
            }
            if (response.has("whole30")) {
                recipeFull.setWhole30(response.getBoolean("whole30"));
            }
            if (response.has("preparationMinutes")) {
                recipeFull.setPreparationMinutes(response.getInt("preparationMinutes"));
            }
            if (response.has("cookingMinutes")) {
                recipeFull.setCookingMinutes(response.getInt("cookingMinutes"));
            }
            if (response.has("sourceUrl")) {
                recipeFull.setSourceUrl(response.getString("sourceUrl"));
            }
            if (response.has("healthScore")) {
                recipeFull.setHealthScore(response.getString("healthScore"));
            }
            if (response.has("pricePerServing")) {
                recipeFull.setPricePerServing(response.getInt("pricePerServing"));
            }
            if (response.has("title")) {
                recipeFull.setTitle(response.getString("title"));
            }
            if (response.has("readyInMinutes")) {
                recipeFull.setReadyInMinutes(response.getInt("readyInMinutes"));
            }
            if (response.has("image")) {
                recipeFull.setImage(response.getString("image"));
            }

            JSONArray ingredients_json = response.getJSONArray("extendedIngredients");
            List<Ingredient> fullIngredients = new ArrayList<>();
            for (int i = 0; i < ingredients_json.length(); ++i) {
                Ingredient tempIngredient = new Ingredient();
                JSONObject numIngredient = ingredients_json.getJSONObject(i);
                tempIngredient.setAmount(numIngredient.getInt("amount"));
                tempIngredient.setName(numIngredient.getString("name"));
                tempIngredient.setUnit(numIngredient.getString("unit"));

                fullIngredients.add(tempIngredient);
            }
            recipeFull.setIngredients_list(fullIngredients);

            JSONObject nutritions = response.getJSONObject("nutrition");
            JSONArray nutrients = nutritions.getJSONArray("nutrients");
            Nutrition tempNutrition = new Nutrition();
            for (int i = 0; i < nutrients.length(); ++i) {
                JSONObject tempNutrient = nutrients.getJSONObject(i);

                switch (tempNutrient.getString("title")) {
                    case "Calories":
                        tempNutrition.setCalories(tempNutrient.getInt("amount"));
                    case "Fat":
                        tempNutrition.setFat(tempNutrient.getInt("amount"));
                    case "Saturated Fat":
                        tempNutrition.setSaturatedFat(tempNutrient.getInt("amount"));
                    case "Carbohydrates":
                        tempNutrition.setCarbohydrates(tempNutrient.getInt("amount"));
                    case "Sugar":
                        tempNutrition.setSugar(tempNutrient.getInt("amount"));
                    case "Cholesterol":
                        tempNutrition.setCholesterol(tempNutrient.getInt("amount"));
                    case "Protein":
                        tempNutrition.setProtein(tempNutrient.getInt("amount"));
                    case "Sodium":
                        tempNutrition.setSodium(tempNutrient.getInt("amount"));
                }
            }
            recipeFull.setNutrition(tempNutrition);
            recipeIDFull = recipeFull;

        } catch (Exception e) {
            Log.d("ERROR_CHECKER", e.getMessage());
        }
    }
}
