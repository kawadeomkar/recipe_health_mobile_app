package com.example.recipe_app;

public class RecipeTemp {
    private String id;
    private String title;
    private String usedIngredientCount;
    private String missedIngredientCount;
    private String image;
    private String calories;
    private String protein;
    private String fat;
    private String carbs;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public String getTitle() {
        return title;
    }

    public String getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMissedIngredientCount(String missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUsedIngredientCount(String usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public String getCalories() {
        return calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }
}
