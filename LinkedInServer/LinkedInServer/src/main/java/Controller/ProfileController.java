package Controller;

import Database.DbController;
import Model.ProfileContactInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProfileController {
    public static boolean lengthChecker(String text, int limit) {
        if(text.length() > limit) {
            return false;
        }
        return true;
    }



//    public static boolean
}
