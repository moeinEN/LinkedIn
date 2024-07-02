package Controller;


import Model.*;
import Model.Requests.*;
import Model.Response.ProfileResponse;
import Model.User;
import Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.FilenameUtils;

import static Controller.FileController.writeResponseBodyToDisk;

public class RetrofitBuilder {

    private static final String BASE_URL = "http://localhost:8080";
    private Retrofit retrofit = retrofitBuilder();

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

    public String syncCallSayHello(){
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callSync = service.sayHello();
        String serverResponse;
        try {
            Response<ResponseBody> response = callSync.execute();
            byte[] responseBodyBytes = response.body().bytes();
            Gson gson = new Gson();
            serverResponse = gson.fromJson(new String(responseBodyBytes), String.class);
            return serverResponse;
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

    public Messages syncCallLogin(LoginCredentials loginCredentials) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLogin = service.login(loginCredentials);
        String responseString;
        try {
            Response<ResponseBody> response = callLogin.execute();
            if (response.isSuccessful() && response.body() != null) {
                byte[] responseBodyBytes = response.body().bytes();
                Gson gson = new Gson();
                responseString = gson.fromJson(new String(responseBodyBytes), String.class);
                Cookies.setSessionToken(responseString);
            } else {
                return Messages.INTERNAL_ERROR;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
        return Messages.USER_LOGGED_IN_SUCCESSFULLY;
    }

    public Messages syncCallProfile(CreateProfileRequest profile){
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callProfile = service.profile(profile, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callProfile.execute();
            byte[] responseBodyBytes = response.body().bytes();
            Gson gson = new Gson();
            ServerResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);

            return ServerResponse;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallLike(LikeRequest like) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLike = service.like(like, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callLike.execute();
            byte[] responseBodeBytes = response.body().bytes();
            Gson gson = new Gson();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallComment(CommentRequest comment) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callLike = service.comment(comment, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callLike.execute();
            byte[] responseBodeBytes = response.body().bytes();
            Gson gson = new Gson();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public Messages syncCallConnect(ConnectRequest connectRequest) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callConnect = service.connect(connectRequest, Cookies.getSessionToken());
        Messages serverResponse;
        try {
            Response<ResponseBody> response = callConnect.execute();
            byte[] responseBodyBytes = response.body().bytes();
            Gson gson = new Gson();
            serverResponse = gson.fromJson(new String(responseBodyBytes), Messages.class);

            return serverResponse;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }

    }

    //TODO add size limit to uploaded files
    //TODO filenames
    public void asyncCallUpload(String filePath) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", FileController.generateFileName() + FilenameUtils.getExtension(file.getName()), requestFile);


        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> call = service.upload(body);
        Messages message = null;
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("Upload successful: " + response.body());
                } else {
                    System.out.println("Upload failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void asyncCallDownload(String filename) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> call = service.downloadFile(filename);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean success = writeResponseBodyToDisk(response.body(), filename);
                    if (success) {
                        System.out.println("File downloaded successfully");
                    } else {
                        System.out.println("Failed to save the file");
                    }
                } else {
                    System.out.println("Server returned an error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public Messages acceptConnection(AcceptConnection acceptConnection){
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callAcceptConnection = service.acceptConnection(acceptConnection, Cookies.getSessionToken());
        Messages ServerResponse;
        try {
            Response<ResponseBody> response = callAcceptConnection.execute();
            byte[] responseBodeBytes = response.body().bytes();
            Gson gson = new Gson();
            ServerResponse = gson.fromJson(new String(responseBodeBytes), Messages.class);

            return ServerResponse;
        } catch (Exception ex) {
            ex.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }

    public ProfileResponse watchProfileRequest(WatchProfileRequest watchProfileRequest) {
        UserService service = retrofit.create(UserService.class);
        Call<ResponseBody> callWatchProfile = service.watchProfile(watchProfileRequest, Cookies.getSessionToken());
        ProfileResponse ServerResponse;
        try {
            Response<ResponseBody> response = callWatchProfile.execute();
            if (response.isSuccessful()) {
                byte[] responseBodeBytes = response.body().bytes();
                Gson gson = new Gson();
                ServerResponse = gson.fromJson(new String(responseBodeBytes), ProfileResponse.class);

                return ServerResponse;
            }
            else {
                return null;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
}