package com.example.recipe_app;

public class Ingredient {

    private String name;
    private int amount;
    private String unit;

    public int getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
