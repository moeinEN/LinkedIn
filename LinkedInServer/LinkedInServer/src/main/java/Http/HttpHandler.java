package Http;

import Controller.RequestHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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

        server.createContext("/user/register", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                RequestHandler.registerHandler(exchange);
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

        server.createContext("/like", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.likeHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/comment", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.commentHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/connect", new com.sun.net.httpserver.HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.connectRequestHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/user/profile", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.profileHandler(exchange);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/upload", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.uploadHandler(exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/files", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.downloadHandler(exchange);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/acceptConnection", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.acceptConnectionHandler(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/watchProfile", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.watchProfile(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/acceptConnection", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.acceptConnectionHandler(exchange);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/searchPost", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.searchPost(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/searchProfile", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.searchProfile(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/watchConnections", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.watchConnections(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/watchPendingConnections", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.watchPendingConnections(exchange);
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        server.createContext("/post", new com.sun.net.httpserver.HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    RequestHandler.newPost(exchange);
                }
                catch (Exception e) {
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
