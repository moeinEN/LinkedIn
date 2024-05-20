package Controllers;

import Database.DatabaseQueryController;
import Database.DbController;
import Model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequestHandler {
    public static void helloHandler(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            System.out.println("got it");
            JSONObject json = new JSONObject();
            try {
                json.put("Message","Hello, received your GET request!");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String jsonResponse = json.toString();
            byte[] responseBytes = jsonResponse.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length); // use the actual length of the response body
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    public static void userHandler(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            System.out.println("got it");
            User user;
            try {
                user = new User();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            byte[] responseBytes = user.toByte("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length); // use the actual length of the response body
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    public static void registerHandler(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            //TODO better to add a logger for debugging purposes (GondeGoozi Nashta)
            System.out.println("got it");

            User user = null;
            try (InputStream requestBody = exchange.getRequestBody();
                 InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                Gson gson = new Gson();
                user = gson.fromJson(reader, User.class);

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
                return;
            }
            System.out.println("Received user: " + user);
            String result = DatabaseQueryController.addUser(user.getUsername(), user.getPassword(), user.getEmail());
            byte[] responseBytes = result.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length); // use the actual length of the response body
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
