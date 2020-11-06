package com.example.payo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {
    @GET("/api/users?")
    Call<JsonObjectAPI> doGetUserList(@Query("page") int page);
}
