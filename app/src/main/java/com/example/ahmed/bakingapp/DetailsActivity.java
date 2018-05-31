package com.example.ahmed.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ahmed.bakingapp.data.Ingredient;
import com.example.ahmed.bakingapp.data.Step;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    public static ArrayList<Ingredient> ingredient;
    public static ArrayList<Step> steps;
    public static boolean mTwoPanel;
    static String name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_fragment);
        name = getIntent().getStringExtra("name");
        setTitle(name);

        mTwoPanel = findViewById(R.id.linear_layout2) != null;


//        ingredient = (ArrayList<Ingredient>) getIntent().getExtras().get("ingredients");
//        steps = (ArrayList<Step>) getIntent().getExtras().get("steps");
//        Log.d("steps", "onCreate: steps(details) = " + steps);
//        Log.d("video", "onCreate: video(details) = " + steps.get(MainActivity.listId).getVideoURL());

    }
}
