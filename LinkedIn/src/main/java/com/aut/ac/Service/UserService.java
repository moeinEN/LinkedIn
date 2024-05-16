package com.aut.ac.Service;

import com.aut.ac.Model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserService {

    @POST("/add/{username}")
    public Call<User> addUser(@Path("username") String username, @Body User user);

    @GET("/users")
    public Call<List<User>> getUsers();

    @GET("/users/{username}")
    public Call<User> getUser(@Path("username") String username);
}