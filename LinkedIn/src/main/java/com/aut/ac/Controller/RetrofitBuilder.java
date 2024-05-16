package com.aut.ac.Controller;

import com.aut.ac.Model.User;
import com.aut.ac.Service.UserService;
import com.aut.ac.Service.UserServiceImp;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://localhost:8080";


    private Retrofit retrofitBuilder(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    private User syncCallGetUserService(String username){
        Retrofit retrofit = this.retrofitBuilder();
        UserServiceImp service = retrofit.create(UserServiceImp.class);
        Call<User> callSync = service.getUser(username);

        try {
            Response<User> response = callSync.execute();
            User user = response.body();
            return user;
        } catch (Exception ex) { return null; }
    }

    private void asyncCallGetUserService(String username){
        Retrofit retrofit = this.retrofitBuilder();
        UserServiceImp service = retrofit.create(UserServiceImp.class);
        Call<User> callAsync = service.getUser(username);
        callAsync.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                System.out.println(user.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                System.out.println(throwable);
            }
        });
    }

}
