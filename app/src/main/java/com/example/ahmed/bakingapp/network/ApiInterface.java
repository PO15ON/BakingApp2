package com.example.ahmed.bakingapp.network;

import com.example.ahmed.bakingapp.data.Datum;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<List<Datum>> getRecipeCards();
}
