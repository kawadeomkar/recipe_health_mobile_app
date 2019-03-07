package com.example.recipe_app;

public class RecipeTemp {
    private String id;
    private String title;
    private String usedIngredientCount;
    private String missedIngredientCount;
    private String image;

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
}
