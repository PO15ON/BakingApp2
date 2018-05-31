package com.example.ahmed.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ahmed.bakingapp.data.Step;

import java.util.ArrayList;

import static com.example.ahmed.bakingapp.DetailsActivity.mTwoPanel;

public class RecipeStepsFragment extends Fragment {
    private ArrayList<Step> modelList = new ArrayList<>();

    public RecipeStepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.recipe_details_grid_view, container, false);

//        mTwoPanel = rootView.findViewById(R.id.linear_layout2) != null;


        GridView gridView = rootView.findViewById(R.id.details_grid_view);

        modelList = MainActivity.stepsList.getSteps();
        Log.d("video", "onCreateView: " + modelList.get(MainActivity.listId).getVideoURL());
        StepsAdapter mAdapter = new StepsAdapter(getContext(), modelList);
        gridView.setAdapter(mAdapter);
        mAdapter.updateList(modelList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String shortDescription = modelList.get(i).getShortDescription();
                String description = modelList.get(i).getDescription();
                String videoUrl = modelList.get(i).getVideoURL();
                Integer stepId = modelList.get(i).getId();

                Toast.makeText(getContext(), "" + mTwoPanel, Toast.LENGTH_SHORT).show();
                Log.d("video", "onItemClick: videoPre = " + videoUrl);
                Bundle bundle = new Bundle();
                bundle.putString("short", shortDescription);
                bundle.putString("description", description);
                bundle.putString("video", videoUrl);
                bundle.putInt("stepId", stepId);
                bundle.putString("name", DetailsActivity.name);
                bundle.putInt("length", modelList.size());
                if (mTwoPanel) {
                    Toast.makeText(getContext(), "Two Panel", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();
                    stepDescriptionFragment.setArguments(bundle);

                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, stepDescriptionFragment)
                            .commit();
                } else {
                    Toast.makeText(getContext(), "One Panel", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), Main2Activity.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);

                }
            }
        });
        return rootView;
    }
}
