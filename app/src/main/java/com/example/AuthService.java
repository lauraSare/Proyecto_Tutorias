package com.example;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/login")
        // Replace with your API endpoint
    Call<JsonObject> login(@Body JsonObject loginData);
}
