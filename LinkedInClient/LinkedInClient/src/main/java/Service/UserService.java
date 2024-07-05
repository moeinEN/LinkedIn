package Service;

import Model.Post;
import Model.Requests.*;
import Model.Response.WatchConnectionPendingLists;
import Model.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserService {

    @POST("/add/{username}")
    public Call<User> addUser(@Path("username") String username, @Body User user);

    //test endpoint
    @GET("/hello")
    public Call<ResponseBody> sayHello();

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
    @GET("/files/{filename}")
    Call<ResponseBody> downloadFile(@Path("filename") String filename);

    @POST("/connect")
    Call<ResponseBody> connect(@Body ConnectRequest connectRequest, @Header("sessionToken") String sessionToken);

    @POST("/acceptConnection")
    Call<ResponseBody> acceptConnection(@Body AcceptConnection acceptConnection, @Header("sessionToken") String sessionToken);

    @POST("/watchProfile")
    Call<ResponseBody> watchProfile(@Body WatchProfileRequest watchProfile, @Header("sessionToken") String sessionToken);

    @POST("/searchPost")
    Call<ResponseBody> searchPost(@Body SearchPostsRequest searchPostsRequest, @Header("sessionToken") String sessionToken);

    @POST("/searchProfile")
    Call<ResponseBody> searchProfile(@Body SearchProfileRequest searchProfileRequest, @Header("sessionToken") String sessionToken);

    @GET("/watchConnections")
    Call<ResponseBody> watchConnections(@Header("sessionToken") String sessionToken);

    @GET("/watchPendingConnections")
    Call<ResponseBody> watchPendingConnections(@Header("sessionToken") String sessionToken);

    @POST("/post")
    Call<ResponseBody> post(@Body Post post, @Header("sessionToken") String sessionToken);

    @GET("/validateToken")
    Call<ResponseBody> validateToken(@Header("sessionToken") String sessionToken);

    @GET("/getWatchList")
    Call<ResponseBody> getWatchList(@Header("sessionToken") String sessionToken);
}