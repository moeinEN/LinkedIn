import Database.DatabaseQueryController;
import Database.DbController;
import Http.HttpHandler;
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

public class Main {

    public static void main(String[] args) throws Exception {
        DatabaseQueryController.createTableUsers();

        // Create HTTP server listening on port 8080
        HttpServer httpServer = HttpHandler.makeConnectionPoint("localhost", 8080);
        HttpHandler.createContext(httpServer);
    }
}