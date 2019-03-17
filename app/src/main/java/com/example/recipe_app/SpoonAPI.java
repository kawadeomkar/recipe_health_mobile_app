package com.example.recipe_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SpoonAPI  {
    // urls for API calls
    private String url_complexRecipe = "https://spoonacular-recipe-food-nutrition-v1." +
            "p.rapidapi.com/recipes/searchComplex";
    private String headerKey = "vKls1ysqi6mshp5kJSbAidp6k9CVp14y8mFjsn0fKHab0671nS";
    private String url_getRecipeWithID1 = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi." +
            "com/recipes/";
    private String url_getRecipeWithID2 = "/information?includeNutrition=true";
    // API return variables
    private List<RecipeTemp> recipeComplex;
    private RecipeFull recipeFull;


    // get recipeFull after helper function is finished
    public RecipeFull getRecipeFull() {
        return recipeFull;
    }

    // get recipeComplex list of recipes when helper function is finished
    public List<RecipeTemp> getRecipeComplex() {
        return recipeComplex;
    }

    // returns url query for getting a recipe's full information given ID
    public String getRecipeIDURL(String id) {
        recipeFull = new RecipeFull();
        String url_query = url_getRecipeWithID1 + id  + url_getRecipeWithID2;
        return url_query;
    }

    // return header key
    public String getHeaderKey() {
        return headerKey;
    }

    /* returns url query for getting list of recipes (not full information, meant for displaying)
    given ingredients and number of results wanted
    url_query: adds on parameters
    result: returns a list of recipe objects
     */
    public String getRecipeComplexURL(List<String> ingredients, int number) {
        recipeComplex = new ArrayList<>();
        String ranking = "0";
        String offset = "0";
        String limitLicense = "false";
        String instructionsRequired = "true";
        // some parameters to add later
        String excludeIngredients;
        String intolerances;
        String diet; // vegan, keto etc
        String minFat = "0";
        String minCalories = "0";
        String minProtein = "0";
        String minCarbs = "0";
        String url_query = url_complexRecipe + "?number=" + Integer.toString(number) + "&minFat=" +
                minFat + "&minCalories=" + minCalories + "&minProtein=" + minProtein + "&minCarbs="
                + minCarbs + "&ranking=" + ranking + "&offset=" + offset + "&limitLicense="
                + limitLicense + "&instructionsRequired="
                + instructionsRequired + "&includeIngredients=";

        // add on ingredients to url string
        if (ingredients.size() > 1) {
            url_query += ingredients.get(0);
            for (int i = 0; i < ingredients.size()-1; ++i) {
                url_query += "%2C+" + ingredients.get(i);
            }
        } else if (ingredients.size() == 1) {
            url_query += ingredients.get(0);
        } else { // TODO: case if ingredients list is empty -> perhaps throw error earlier if null

        }

        return url_query;
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
                Log.d("ERROR_CHECKER: ", "What's the image URL? " +
                        recipe.getImage());
                recipe.setMissedIngredientCount(recipe_object
                        .getString("missedIngredientCount"));
                recipe.setUsedIngredientCount(recipe_object
                        .getString("usedIngredientCount"));
                recipe.setTitle(recipe_object.getString("title"));

                // only included if min/max macros are set in API request call
                if (recipe_object.has("calories")) {
                    recipe.setCalories(recipe_object.getString("calories"));
                }
                if (recipe_object.has("carbs")) {
                    recipe.setCarbs(recipe_object.getString("carbs"));
                }
                if (recipe_object.has("fat")) {
                    recipe.setFat(recipe_object.getString("fat"));
                }
                if (recipe_object.has("protein")) {
                    recipe.setProtein(recipe_object.getString("protein"));
                }

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
            if (response.has("servings")) {
                recipeFull.setServings(response.getInt("servings"));
            }
            if (response.has("instructions")) {
                recipeFull.setInstructions(response.getString("instructions"));
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
        } catch (Exception e) {
            Log.d("ERROR_CHECKER", e.getMessage());
        }
    }
}
