package Http;

import Controllers.RequestHandler;
import Database.DbController;
import Model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HttpHandler {

    public static HttpServer makeConnectionPoint(String ip, int port) throws IOException {
        return HttpServer.create(new InetSocketAddress(ip, port), 0);
    }

    public static void createContext (HttpServer httpServer) throws IOException {

        HttpServer server = httpServer;
        server.createContext("/hello", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                RequestHandler.helloHandler(exchange);
            }
        });

        server.createContext("/user", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                RequestHandler.userHandler(exchange);
            }
        });

        server.createContext("/user/register", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                RequestHandler.registerHandler(exchange);
            }
        });

        // Handle GET requests at /bye
        server.createContext("/bye", new com.sun.net.httpserver.HttpHandler() {
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

        server.createContext("/login", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.loginHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Start the server
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }
}
