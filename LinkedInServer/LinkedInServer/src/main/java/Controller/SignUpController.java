package Controller;

import Model.Messages;

public class SignUpController {
    // Validate email address : example@gmail.com
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    // Password must contain at least 8 characters include a lowercase and an uppercase alphabet and at least a number
    public static boolean isValidPassword(String password) {
        String pPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pPattern);
        java.util.regex.Matcher m = p.matcher(password);
        return m.matches();
    }

    // Name or familyName must not contain symbols except (') or (.)
    public static boolean isValidName(String name) {
        String nPattern = "^(([ ,.'-](?<!( {2}|[,.'-]{2})))*[A-Za-z])+[ ,.'-]?$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(nPattern);
        java.util.regex.Matcher m = p.matcher(name);
        return m.matches();
    }

    /* Username is 8-20 characters long
       no _ or . at the beginning
       no __ or _. or ._ or .. inside
       no _ or . at the end
       allowed characters are (a-z), (A-Z), (0-9), (.) and (_)
    */
    public static boolean isValidUserName(String userName) {
        String uPattern = "^(?=.{8,32}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(uPattern);
        java.util.regex.Matcher m = p.matcher(userName);
        return m.matches();
    }


    public static Messages validateUserData(String email, String password, String confirmationPassword, String username){
        if(!isValidEmailAddress(email)){
            return Messages.INVALID_EMAIL;
        }
        if(!isValidPassword(password)){
            return Messages.INVALID_PASSWORD;
        }
        if(!isValidName(username)){
            return Messages.INVALID_USERNAME;
        }
        if(!confirmationPassword.equals(password)){
            return Messages.CONFIRMATION_PASSWORD;
        }
        return Messages.SUCCESS;
    }
}