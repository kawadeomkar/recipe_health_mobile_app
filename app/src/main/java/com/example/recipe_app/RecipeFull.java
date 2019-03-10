package com.example.recipe_app;

import java.util.List;

public class RecipeFull {
    private boolean vegetarian;
    private boolean vegan;
    private boolean glutenFree;
    private boolean dairyFree;
    private boolean veryHealthy;
    private boolean cheap;
    private boolean veryPopular;
    private boolean ketogenic;
    private boolean lowFodmap;
    private boolean whole30;
    private int id;
    private int preparationMinutes;
    private int cookingMinutes;
    private int pricePerServing;
    private int readyInMinutes;
    private int servings;
    private String title;
    private String image;
    private String sourceUrl;
    private String healthScore;
    private String sourceName;
    private String instructions;
    private Nutrition nutrition;
    private List<String> diets; // converts from list of map<int, string>
    private List<String> cuisines; // converts from list of map<int, string>
    private List<Ingredient> ingredients_list;

    public boolean isCheap() { return cheap; }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public boolean isKetogenic() {
        return ketogenic;
    }

    public boolean isLowFodmap() {
        return lowFodmap;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isVeryHealthy() {
        return veryHealthy;
    }

    public boolean isVeryPopular() {
        return veryPopular;
    }

    public boolean isWhole30() {
        return whole30;
    }

    public int getCookingMinutes() {
        return cookingMinutes;
    }

    public int getId() {
        return id;
    }

    public int getPreparationMinutes() {
        return preparationMinutes;
    }

    public int getPricePerServing() {
        return pricePerServing;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getHealthScore() {
        return healthScore;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getInstructions() {
        return instructions;
    }

    public Nutrition getNutrition() { return nutrition; }

    public List<Ingredient> getIngredients_list() {
        return ingredients_list;
    }

    public List<String> getCuisines() {
        return cuisines;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCheap(boolean cheap) {
        this.cheap = cheap;
    }

    public void setCookingMinutes(int cookingMinutes) {
        this.cookingMinutes = cookingMinutes;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public void setCuisines(List<String> cuisines) {
        this.cuisines = cuisines;
    }

    public void setHealthScore(String healthScore) {
        this.healthScore = healthScore;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public void setIngredients_list(List<Ingredient> ingredients_list) {
        this.ingredients_list = ingredients_list;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setKetogenic(boolean ketogenic) {
        this.ketogenic = ketogenic;
    }

    public void setLowFodmap(boolean lowFodmap) {
        this.lowFodmap = lowFodmap;
    }

    public void setPreparationMinutes(int preparationMinutes) {
        this.preparationMinutes = preparationMinutes;
    }

    public void setPricePerServing(int pricePerServing) {
        this.pricePerServing = pricePerServing;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public void setVeryHealthy(boolean veryHealthy) {
        this.veryHealthy = veryHealthy;
    }

    public void setVeryPopular(boolean veryPopular) {
        this.veryPopular = veryPopular;
    }

    public void setWhole30(boolean whole30) {
        this.whole30 = whole30;
    }

    public void setNutrition(Nutrition nutrition) { this.nutrition = nutrition; }
}
