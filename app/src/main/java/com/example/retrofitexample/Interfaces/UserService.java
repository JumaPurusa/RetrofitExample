package com.example.retrofitexample.Interfaces;

import com.example.retrofitexample.Models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    public static final String BASE_URL = "https://api.github.com/";

    @GET("/users")
    public Call<List<User>> getUsers(
            @Query("per_page") int per_page,
            @Query("page") int page);

    @GET("/users/{username}")
    public Call<User> getUser(@Path("username") String username);


}
