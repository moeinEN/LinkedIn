import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.SignUpMessages;

public class Main {
    public static void main(String[] args) {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

        System.out.println(retrofitBuilder.syncCallSayHello().toString());
        System.out.println(retrofitBuilder.syncCallGetUser().toString());

        SignUpMessages signUpMessages = null;
        if((signUpMessages = SignUpController.validateUserData("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")) == SignUpMessages.SUCCESS) {
            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("mmd23@yahoo.com", "tEST@123", "tEST@123", "Goostavo")));
        }
        else {
            System.out.println(signUpMessages.message);
        }

        if((signUpMessages = SignUpController.validateUserData("kosanat3221@yahoo.com", "teteEST@123", "teteEST@123", "Kosmadar")) == SignUpMessages.SUCCESS) {
            System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.newUser("kosanat3221@yahoo.com", "teteEST@123", "teteEST@123", "Kosmadar")));
        }
        else {
            System.out.println(signUpMessages.message);
        }
    }
}
