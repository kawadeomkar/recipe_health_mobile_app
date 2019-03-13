package com.example.recipe_app;

public class Nutrition {
    private int calories;
    private int fat;
    private int saturatedFat;
    private int carbohydrates;
    private int sugar;
    private int cholesterol;
    private int sodium;
    private int protein;

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getCalories() {
        return calories;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public int getFat() {
        return fat;
    }

    public int getProtein() {
        return protein;
    }

    public int getSaturatedFat() {
        return saturatedFat;
    }

    public int getSodium() {
        return sodium;
    }

    public int getSugar() {
        return sugar;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public void setSaturatedFat(int saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }
}
