package Controller;


import Model.User;
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

public class RetrofitBuilder {

    private static final String BASE_URL = "http://localhost:8080";


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

    public String syncCallSignUp(User user){
        Retrofit retrofit = this.retrofitBuilder();
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callSync = service.signUp(user);

        try {
            Response<ResponseBody> response = callSync.execute();
            return response.body().string();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null; }
    }
}
