import Controller.RetrofitBuilder;
import Model.Cookies;
import Model.Requests.LoginCredentials;
import Model.Messages;
import Model.Requests.CreateProfileRequest;

public class Main {
    public static void main(String[] args) {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

//        System.out.println(retrofitBuilder.syncCallSayHello().toString());
//        System.out.println(retrofitBuilder.syncCallGetUser().toString());
//
//        Messages signUpMessages = null;
//        if((signUpMessages = SignUpController.validateUserData("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")) == Messages.SUCCESS) {
//            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")).message);
//        }
//        else {
//            System.out.println(signUpMessages.message);
//        }
//
//        if((signUpMessages = SignUpController.validateUserData("whatthehell@yahoo.com", "teteEST@123", "teteEST@123", "testProject")) == Messages.SUCCESS) {
//            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("whatthehell@yahoo.com", "teteEST@123", "teteEST@123", "testProject")).message);
//        }
//        else {
//            System.out.println(signUpMessages.message);
//        }


        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        Messages loginResp = retrofitBuilder.syncCallLogin(loginCredentials);
        System.out.println(Cookies.getSessionToken());


        CreateProfileRequest profile = new CreateProfileRequest();
    }
}
