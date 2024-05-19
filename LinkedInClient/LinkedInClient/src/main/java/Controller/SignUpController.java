package Controller;

import Model.User;

public class SignUpController {
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidPassword(String password) {
        String pPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pPattern);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isValidName(String name) {
        String nPattern = "^(([ ,.'-](?<!( {2}|[,.'-]{2})))*[A-Za-z])+[ ,.'-]?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(nPattern);
        java.util.regex.Matcher m = p.matcher(name);
        return m.matches();
    }

    public static User checkUser(String email, String password, String confirmationPassword, String name, String familyName) {
        if(isValidEmailAddress(email) && isValidPassword(password) && isValidName(name) && isValidName(familyName)) {
            if(confirmationPassword.equals(password)) {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                user.setName(name);
                user.setFamilyName(familyName);
                user.setId(1);
                return user;
            }
        }
        return null;
    }
}
