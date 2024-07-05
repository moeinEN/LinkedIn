import Controller.JwtHandler;
import Database.DatabaseQueryController;
import Database.DbController;
import Http.HttpHandler;
import Model.MiniProfile;
import Model.Requests.SearchProfileRequest;
import Model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import static Database.DatabaseQueryController.*;

public class Main {

    public static void main(String[] args) throws Exception {
        //DatabaseQueryController.createTableUsers();

        //Create HTTP server listening on port 8080
        createTableUsers();
        createTableTokens();
        createTableUserWatchList();
        createTableConnections();
        createTablePost();
        createTableComment();
        createTableLike();
        createTablePendingConnect();
        createTableConnect();
        createTableFollow();
        createTableProfile();
        createTableProfileSports();
        createTableProfileVoluntaryActivities();
        createTableProfileExperience();
        createTableProfileSkills();
        createTableProfileOrganizations();
        createTableProfileJob();
        createTableProfileEducation();
        createTableProfileContactsInfo();
        createTableProfileHeader();
        createTableCertificate();
        createTableUserWatchList();
        createTableNotification();


        HttpServer httpServer = HttpHandler.makeConnectionPoint("localhost", 8080);
        HttpHandler.createContext(httpServer);
    }
}