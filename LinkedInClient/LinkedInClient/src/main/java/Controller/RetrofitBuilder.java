package Controller;


import Model.*;
import Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://127.0.0.1:8080";


    private Retrofit retrofitBuilder(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Gson gson = new GsonBuilder()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    private User syncCallGetUserService(String username){
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<User> callSync = service.addUser(username, new User());

        try {
            Response<User> response = callSync.execute();
            User user = response.body();
            return user;
        } catch (Exception ex) { return null; }
    }

    public User syncCallGetUser(){
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<User> callSync = service.getUser("test");

        try {
            Response<User> response = callSync.execute();
            User user = response.body();
            return user;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null; }
    }

    public JsonObject syncCallSayHello(){
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<JsonObject> callSync = service.sayHello();

        try {
            Response<JsonObject> response = callSync.execute();
            JsonObject string = response.body();
            return string;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null; }
    }

//    public JsonObject syncCallRegister(String username, String password){
//        Retrofit retrofit = this.retrofitBuilder();
//        UserService service = retrofit.create(UserService.class);
//        Call<JsonObject> callSync = service.regUser(username, password);
//
//        try {
//            Response<JsonObject> response = callSync.execute();
//            JsonObject string = response.body();
//            return string;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null; }
//    }

    public Messages syncCallSignUp(RegisterCredentials registerCredentials){
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callSync = service.signUp(registerCredentials);

        try {
            Response<ResponseBody> response = callSync.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodyBytes = response.body().bytes();
                return Messages.fromByte(responseBodyBytes);
            } else {
                return Messages.INTERNAL_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public String syncCallLogin(LoginCredentials loginCredentials) {
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLogin = service.login(loginCredentials);

        try {
            Response<ResponseBody> response = callLogin.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodyBytes = response.body().bytes();
                Gson gson = new Gson();
                String responseString = gson.fromJson(new String(responseBodyBytes), String.class);
                Cookies.setSessionToken(responseString);
            } else {
                return Messages.INTERNAL_ERROR.message;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return "";
    }
}
