package Model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Cookies {
    private static String sessionToken;
    private static String profileIdentification;

    public static void setSessionToken(String sessionToken) {
        Cookies.sessionToken = sessionToken;
    }

    public static String getSessionToken() {
        return sessionToken;
    }

    public static String getProfileIdentification() {
        return profileIdentification;
    }

    public static void setProfileIdentification(String profileIdentification) {
        Cookies.profileIdentification = profileIdentification;
    }
}
