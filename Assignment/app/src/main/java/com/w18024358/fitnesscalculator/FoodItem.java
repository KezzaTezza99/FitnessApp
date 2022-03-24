package com.w18024358.fitnesscalculator;

public class FoodItem
{
    private String itemQuantity;
    private String itemName;
    private String itemCalories;

    public FoodItem(String quantity, String item, String calories)
    {
        this.itemQuantity = quantity;
        this.itemName = item;
        this.itemCalories = calories;
    }

    //Getter and Setter Methods
    public String getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCalories() {
        return itemCalories;
    }

    public void setItemCalories(String itemCalories) {
        this.itemCalories = itemCalories;
    }
}