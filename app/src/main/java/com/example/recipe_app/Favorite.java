package com.example.recipe_app;

public class Favorite {
    private String recipeID;
    private String title;
    private String image;

    public String getTitle() {
        return title;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
}
