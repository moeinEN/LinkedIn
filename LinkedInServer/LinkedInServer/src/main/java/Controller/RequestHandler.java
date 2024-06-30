package Controller;

import Database.DatabaseQueryController;
import Model.*;
import Model.Requests.LoginCredentials;
import Model.Requests.RegisterCredentials;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import static Model.Messages.*;

public class RequestHandler {

    public static Logger logger = Logger.getLogger("RequestHandler");

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

            RegisterCredentials registerCredentials = null;
            try (InputStream requestBody = exchange.getRequestBody();
                 InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                Gson gson = new Gson();
                registerCredentials = gson.fromJson(reader, RegisterCredentials.class);

            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
                return;
            }

            Messages signUpMessage = SignUpController.validateUserData(registerCredentials.getEmail(), registerCredentials.getPassword(), registerCredentials.getConfirmationPassword(), registerCredentials.getUsername());
            if(signUpMessage == Messages.SUCCESS) {
                signUpMessage = DatabaseQueryController.addUser(registerCredentials.getUsername(), registerCredentials.getPassword(), registerCredentials.getEmail());
            }

            byte[] responseBytes = signUpMessage.toByte("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length); // use the actual length of the response body

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(405, response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void loginHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            //TODO better to add a logger for debugging purposes (GondeGoozi Nashta)

            LoginCredentials recievedLoginCredentials = null;
            try (InputStream requestBody = exchange.getRequestBody();
                 InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                Gson gson = new Gson();
                recievedLoginCredentials = gson.fromJson(reader, LoginCredentials.class);
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1); // 400 Bad Request
                return;
            }

            String response = "";
            int responseCode = 200;

            Messages loginMessage = DatabaseQueryController.checkCredentials(recievedLoginCredentials);
            if(loginMessage == Messages.SUCCESS) {
                User loginUser = DatabaseQueryController.getUser(recievedLoginCredentials.getUsername());
                response = JwtHandler.createJwtToken(((Long) loginUser.getId()).toString(), loginUser.getUsername(), loginUser.getPassword(), loginUser.getEmail(), 3600000L);
            }
            else {
                response = loginMessage.getMessage();
                responseCode = 401;
            }

            byte[] responseBytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(responseCode, responseBytes.length); // use the actual length of the response body

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(405, response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void profileHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            int userId = 0;
            Headers requestHeaders = exchange.getRequestHeaders();
            if (requestHeaders.containsKey("sessionToken")) {
                List<String> sessionTokens = requestHeaders.get("sessionToken");
                String sessionToken = sessionTokens.get(0);
                if(JwtHandler.validateUserSession(sessionToken) == Messages.SUCCESS) {
                    userId = JwtHandler.getUserIdFromJwtToken(sessionToken);
                }
                else {
                    byte[] response = Messages.UNAUTHORIZED.toByte("UTF-8");
                    exchange.sendResponseHeaders(UNAUTHORIZED.getStatusCode(), response.length); // 405 Method Not Allowed
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                }
            } else {
                byte[] response = Messages.UNAUTHORIZED.toByte("UTF-8");
                exchange.sendResponseHeaders(UNAUTHORIZED.getStatusCode(), response.length); // 405 Method Not Allowed
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }

            try (InputStream requestBody = exchange.getRequestBody();
                 InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                Gson gson = new Gson();
                Profile recievedProfile = null;
                try {
                    recievedProfile = gson.fromJson(reader, Profile.class);
                }
                catch (IllegalArgumentException e) {
                    logger.info(e.getMessage());
                    byte[] response = e.getMessage().getBytes("UTF-8");
                    exchange.sendResponseHeaders(INVALID_PROFILE_INPUTS.getStatusCode(), response.length); // 400 Bad Request
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                    return;
                }
                try {
                    DatabaseQueryController.insertProfile(recievedProfile, userId);
                    byte[] response = SUCCESS.toByte("UTF-8");
                    exchange.sendResponseHeaders(SUCCESS.getStatusCode(), response.length); // 400 Bad Request
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                    byte[] response = INTERNAL_ERROR.toByte("UTF-8");
                    exchange.sendResponseHeaders(INTERNAL_ERROR.getStatusCode(), response.length); // 400 Bad Request
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                byte[] response = BAD_REQUEST.toByte("UTF-8");
                exchange.sendResponseHeaders(BAD_REQUEST.getStatusCode(), response.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }

        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void uploadHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);

                // Parse the request
                List<FileItem> items = upload.parseRequest(new HttpExchangeRequestContext(exchange));
                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        // Handle file upload
                        Path uploadDir = Paths.get("src/main/resources/testUpload");
                        if (!Files.exists(uploadDir)) {
                            Files.createDirectories(uploadDir);
                        }
                        Path filePath = uploadDir.resolve(item.getName());
                        Files.copy(item.getInputStream(), filePath);
                    }
                }

                String response = "File uploaded successfully!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, -1);
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }
    public static void downloadHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Extract file name from the request URI
            String requestedFile = exchange.getRequestURI().getPath().replace("/files/", "");
            Path filePath = Paths.get("src/main/resources/testUpload", requestedFile);

            if (Files.exists(filePath)) {
                // Determine the file's content type
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                // Set the response headers
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.getResponseHeaders().set("Content-Disposition", "attachment; filename=\"" + filePath.getFileName().toString() + "\"");
                exchange.sendResponseHeaders(200, Files.size(filePath));

                // Write the file content to the response body
                OutputStream outputStream = exchange.getResponseBody();
                Files.copy(filePath, outputStream);
                outputStream.close();
            } else {
                // File not found
                String response = "File not found";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(response.getBytes());
                outputStream.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }
}
