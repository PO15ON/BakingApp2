package com.example.ahmed.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.ahmed.bakingapp.data.Datum;
import com.example.ahmed.bakingapp.data.Ingredient;
import com.example.ahmed.bakingapp.data.Step;
import com.example.ahmed.bakingapp.network.ApiClient;
import com.example.ahmed.bakingapp.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ahmedd";
    public static int listId;
    public static AbstractModel stepsList;
    public static ArrayList<AbstractModel> modelListAll;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelListAll = new ArrayList<>();

//        recyclerView = findViewById(R.stepId.recycler_view);
        ButterKnife.bind(this);


        mTwoPane = findViewById(R.id.linear_layout) != null;

        setAdapter();

    }


    private void setAdapter() {

        mAdapter = new RecyclerViewAdapter(MainActivity.this, modelListAll);

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<List<Datum>> getRecipeCards = apiInterface.getRecipeCards();
        getRecipeCards.enqueue(new Callback<List<Datum>>() {
            @Override
            public void onResponse(Call<List<Datum>> call, Response<List<Datum>> response) {

                int jsonSize = response.body().size();
                for (int i = 0; i < jsonSize; i++) {
                    String name = response.body().get(i).getName();
                    ArrayList<Ingredient> ingredients = response.body().get(i).getIngredients();
                    ArrayList<Step> steps = response.body().get(i).getSteps();
                    Log.d(TAG, "onResponse: video(Call) = " + steps.get(0).getVideoURL());

                    modelListAll.add(new AbstractModel(name, ingredients, steps));

                    mAdapter.updateList(modelListAll);
                }
            }

            @Override
            public void onFailure(Call<List<Datum>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


        recyclerView.setHasFixedSize(true);

        // use a linear layout manager


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        if (mTwoPane) {
            recyclerView.setLayoutManager(gridLayoutManager);

        } else {
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.divider_recyclerview));
            recyclerView.addItemDecoration(dividerItemDecoration);
        }


        recyclerView.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, AbstractModel model) {
                mAdapter.updateList(modelListAll);
                //handle item click events here
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                listId = position;
                stepsList = model;
                try {
                    intent.putExtra("name", model.getTitle());
                    intent.putExtra("ingredients", model.getIngredients());
                    intent.putExtra("steps", model.getSteps());
                    Log.d(TAG, "onItemClick: " + model.getTitle());
                    Log.d(TAG, "onItemClick: steps size = " + model.getSteps().size() + "\nlistId = " + listId);
                    Log.d(TAG, "onItemClick: video(Main) = " + model.getSteps().get(listId).getVideoURL());
                    Log.d(TAG, "onItemClick: steps = " + model.getSteps());
                    Log.d(TAG, "onItemClick: video(Main2) = " + model.getSteps().get(listId).getVideoURL());
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "onItemClick: ", e);
                }
            }
        });


    }


}
