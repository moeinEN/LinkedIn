package Service;

import Model.Requests.*;
import Model.User;
import com.google.gson.JsonObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserService {

    @POST("/add/{username}")
    public Call<User> addUser(@Path("username") String username, @Body User user);

    //test endpoint
    @GET("/hello")
    public Call<JsonObject> sayHello();

    @GET("/users")
    public Call<List<User>> getUsers();

    @GET("/users/{username}")
    public Call<User> getUser(@Path("username") String username);

    @POST("/user/register")
    Call<ResponseBody> signUp(@Body RegisterCredentials registerCredentials);

    @POST("/login")
    Call<ResponseBody> login(@Body LoginCredentials loginCredentials);

    @POST("/user/profile")
    Call<ResponseBody> profile(@Body CreateProfileRequest profile, @Header("sessionToken") String sessionToken);

    @POST("/like")
    Call<ResponseBody> like(@Body LikeRequest like, @Header("sessionToken") String sessionToken);

    @POST("/comment")
    Call<ResponseBody> comment(@Body CommentRequest comment, @Header("sessionToken") String sessionToken);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    @Streaming
    @GET("files/{filename}")
    Call<ResponseBody> downloadFile(@Path("filename") String filename);

}