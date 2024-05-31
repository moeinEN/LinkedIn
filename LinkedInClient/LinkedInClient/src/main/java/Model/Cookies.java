package Model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cookies {
    private static String sessionToken;


    public static void setSessionToken(String sessionToken) {
        Cookies.sessionToken = sessionToken;
    }
}
