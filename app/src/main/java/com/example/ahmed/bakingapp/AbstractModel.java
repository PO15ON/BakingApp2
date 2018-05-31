package com.example.ahmed.bakingapp;

import com.example.ahmed.bakingapp.data.Ingredient;
import com.example.ahmed.bakingapp.data.Step;

import java.io.Serializable;
import java.util.ArrayList;

public class AbstractModel implements Serializable {

    private String title;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> step;


    public AbstractModel(String title, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.title = title;
        this.ingredients = ingredients;
        this.step = steps;
    }

    public AbstractModel() {

    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return step;
    }

    public void setSteps(ArrayList<Step> step) {
        this.step = step;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
