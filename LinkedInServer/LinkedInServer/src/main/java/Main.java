import Controllers.DbController;
import Model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
        // Create HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        Connection db = null;
        Statement stmt = null;

        db = DbController.getConnection();
        db.setAutoCommit(false);
        System.out.println("Opened database successfully");

        stmt = db.createStatement();


        try {
            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
            stmt.executeUpdate(sql);

            stmt.close();
            db.commit();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
        }
        finally {
            db.close();
        }
        System.out.println("Records created successfully");



        // Handle POST requests at /hello
        server.createContext("/hello", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
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
        });

        server.createContext("/user", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
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
        });

        server.createContext("/user/register", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
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
//                        System.out.println(user.getEmail() + "\n" + user.getPassword() + "\n" + user.getUsername() + "\n" + user.getName() + "\n" + user.getFamilyName());

                    String successMessage = "User registered successfully";
                    byte[] responseBytes = successMessage.getBytes("UTF-8");

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
        });

        // Handle GET requests at /bye
        server.createContext("/bye", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    String response = "Goodbye, received your GET request!";
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    String response = "Method Not Allowed";
                    exchange.sendResponseHeaders(405, response.getBytes().length); // 405 Method Not Allowed
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                }
            }
        });

        // Start the server
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }
}