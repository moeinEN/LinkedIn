package Service;

import Model.User;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserService {

    @POST("/add/{username}")
    public Call<User> addUser(@Path("username") String username, @Body User user);

    @GET("/users")
    public Call<List<User>> getUsers();

    @POST("/user/register")
    public Call<String> signUp(@Body User user);

    @GET("/users/{username}")
    public Call<User> getUser(@Path("username") String username);

    @GET("/hello")
    public Call<JsonObject> sayHello();
}