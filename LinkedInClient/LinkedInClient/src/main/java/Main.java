import Controller.RetrofitBuilder;
import Controller.SignUpController;
import Model.User;

public class Main {
    public static void main(String[] args) {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

        System.out.println(retrofitBuilder.syncCallSayHello().toString());
        System.out.println(retrofitBuilder.syncCallGetUser().toString());

        System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.checkUser("mmd@yahoo.com", "tEST@123", "tEST@123", "Mmd", "Goostavo")));
        System.out.println(retrofitBuilder.syncCallSignUp(SignUpController.checkUser("kosanat@yahoo.com", "teteEST@123", "teteEST@123", "KosMadar", "Kosmadar")));

    }
}
