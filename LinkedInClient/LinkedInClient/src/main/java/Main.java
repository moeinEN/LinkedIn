import Controller.RetrofitBuilder;

public class Main {
    public static void main(String[] args) {
        RetrofitBuilder retrofitBuilder = new RetrofitBuilder();

        System.out.println(retrofitBuilder.syncCallSayHello().toString());
        System.out.println(retrofitBuilder.syncCallGetUser().toString());
    }
}
