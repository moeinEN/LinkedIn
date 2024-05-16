import Model.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        // Create HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

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
                        user = new User(1L, "test", "passTEST");
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