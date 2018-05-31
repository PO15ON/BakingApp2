package com.example.ahmed.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.ahmed.bakingapp.data.Ingredient;

import java.util.ArrayList;

public class RecipeDetailsFragment extends Fragment {

    private ArrayList<Ingredient> modelList;

    public RecipeDetailsFragment() {
        modelList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_details_grid_view, container, false);

        GridView gridView = rootView.findViewById(R.id.details_grid_view);

        modelList = MainActivity.stepsList.getIngredients();
        MasterListAdapter mAdapter = new MasterListAdapter(getContext(), modelList);
        mAdapter.updateList(modelList);
        gridView.setAdapter(mAdapter);
        return rootView;
    }
}
