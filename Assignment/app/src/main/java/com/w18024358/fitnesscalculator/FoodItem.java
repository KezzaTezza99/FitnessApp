package com.w18024358.fitnesscalculator;

public class FoodItem
{
    private int quantity;
    private String item;
    private int calories;

    public FoodItem(int quantity, String item, int calories) {
        this.quantity = quantity;
        this.item = item;
        this.calories = calories;
    }

    //Getters / Setters
    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getItem()
    {
        return item;
    }

    public void setItem(String item)
    {
        this.item = item;
    }

    public int getCalories()
    {
        return calories;
    }

    public void setCalories(int calories)
    {
        this.calories = calories;
    }
}