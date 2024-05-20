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
            String sql = "CREATE TABLE USER (\n" +
                    "    id BIGINT PRIMARY KEY,\n" +
                    "    name VARCHAR(255),\n" +
                    "    familyName VARCHAR(255),\n" +
                    "    username VARCHAR(255) UNIQUE,\n" +
                    "    password VARCHAR(255),\n" +
                    "    email VARCHAR(255) UNIQUE\n" +
                    ");";

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
        System.out.println("Table created successfully");



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

                    Connection db = null;

                    db = DbController.getConnection();
                    try {
                        db.setAutoCommit(false);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Opened database successfully");

                    try {
                        String sql = "INSERT INTO USER (id, name, familyName, username, password, email) " + "VALUES (?, ?, ?, ?, ?, ?);";

                        PreparedStatement pstmt = db.prepareStatement(sql);
                        pstmt.setLong(1, user.getId());
                        pstmt.setString(2, user.getName());
                        pstmt.setString(3, user.getFamilyName());
                        pstmt.setString(4, user.getUsername());
                        pstmt.setString(5, user.getPassword());
                        pstmt.setString(6, user.getEmail());

                        pstmt.executeUpdate();
                        db.commit();

                    } catch ( Exception e ) {
                        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    }
                    finally {
                        try {
                            db.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }


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