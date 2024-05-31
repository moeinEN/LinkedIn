import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.LoginCredentials;
import Model.Messages;

public class Main {
    public static void main(String[] args) {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

        System.out.println(retrofitBuilder.syncCallSayHello().toString());
        System.out.println(retrofitBuilder.syncCallGetUser().toString());

        Messages signUpMessages = null;
        if((signUpMessages = SignUpController.validateUserData("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")) == Messages.SUCCESS) {
            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")).message);
        }
        else {
            System.out.println(signUpMessages.message);
        }

        if((signUpMessages = SignUpController.validateUserData("kosanat3221@yahoo.com", "teteEST@123", "teteEST@123", "Kosmadar")) == Messages.SUCCESS) {
            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("kosanat3221@yahoo.com", "teteEST@123", "teteEST@123", "Kosmadar")).message);
        }
        else {
            System.out.println(signUpMessages.message);
        }


        LoginCredentials loginCredentials = new LoginCredentials("Goostavo", "tEST@123");
        System.out.println(retrofitBuilder.syncCallLogin(loginCredentials));
    }
}
