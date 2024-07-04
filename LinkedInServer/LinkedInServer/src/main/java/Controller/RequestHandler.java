package Controller;

import Database.DatabaseQueryController;
import Database.DbController;
import Model.*;
import Model.Requests.*;
import Model.Response.WatchProfileSearchResults;
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
            String response;
            try {
                response = "Hello, received your GET request!";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            byte[] responseBytes = response.getBytes("UTF-8");
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
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    Profile recievedProfile = null;
                    try {
                        recievedProfile = gson.fromJson(reader, Profile.class);
                        try {
                            DatabaseQueryController.insertProfile(recievedProfile, userId);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void likeHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    LikeRequest likeRequest = null;
                    try {
                        likeRequest = gson.fromJson(reader, LikeRequest.class);
                        try {
                            DatabaseQueryController.insertLike(likeRequest, userId);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void commentHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    CommentRequest commentRequest = null;
                    try {
                        commentRequest = gson.fromJson(reader, CommentRequest.class);
                        try {
                            DatabaseQueryController.insertComment(commentRequest, userId);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void connectRequestHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    ConnectRequest connectRequest = null;
                    try {
                        connectRequest = gson.fromJson(reader, ConnectRequest.class);
                        try {
                            DatabaseQueryController.insertPendingConnect(userId, connectRequest);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
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
    public static void acceptConnectionHandler(HttpExchange exchange) throws IOException, SQLException {
        if ("POST".equals(exchange.getRequestMethod())) {
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    AcceptConnection acceptConnection = null;
                    try {
                        acceptConnection = gson.fromJson(reader, AcceptConnection.class);
                        try {
                            DatabaseQueryController.acceptOrDeclineConnection(userId, acceptConnection);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
    public static void watchProfile(HttpExchange exchange) throws IOException, SQLException {
        if("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                exchange.sendResponseHeaders(401, -1); // 401 unauthorized
                return;
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    WatchProfileRequest watchProfileRequest = null;
                    try {
                        watchProfileRequest = gson.fromJson(reader, WatchProfileRequest.class);
                        try {
                            response = DatabaseQueryController.getWatchProfileRequest(watchProfileRequest).toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            exchange.sendResponseHeaders(400, -1); // 400 bad request
                            return;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        exchange.sendResponseHeaders(400, -1); // 400 bad request
                        return;
                    }
                }
                exchange.sendResponseHeaders(responseCode, response.length); // use the actual length of the response body
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 method not-allowed
            return;
        }
    }
    public static void searchPost(HttpExchange exchange) throws IOException, SQLException {
        if("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                exchange.sendResponseHeaders(401, -1); // 401 unauthorized
                return;
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    SearchPostsRequest searchPostsRequest = null;
                    try {
                        searchPostsRequest = gson.fromJson(reader, SearchPostsRequest.class);
                        try {
                            response = DatabaseQueryController.getPostBySearch(searchPostsRequest).toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            exchange.sendResponseHeaders(400, -1); // 400 bad request
                            return;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        exchange.sendResponseHeaders(400, -1); // 400 bad request
                        return;
                    }
                }
                exchange.sendResponseHeaders(responseCode, response.length); // use the actual length of the response body
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 method not-allowed
            return;
        }
    }
    public static void searchProfile(HttpExchange exchange) throws IOException, SQLException {
        if("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                exchange.sendResponseHeaders(401, -1); // 401 unauthorized
                return;
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    SearchProfileRequest searchProfileRequest = null;
                    try {
                        searchProfileRequest = gson.fromJson(reader, SearchProfileRequest.class);
                        try {
                            response = DatabaseQueryController.getWatchProfileSearchResults(searchProfileRequest).toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            exchange.sendResponseHeaders(400, -1); // 400 bad request
                            return;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        exchange.sendResponseHeaders(400, -1); // 400 bad request
                        return;
                    }
                }
                exchange.sendResponseHeaders(responseCode, response.length); // use the actual length of the response body
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 method not-allowed
            return;
        }
    }
    public static void watchConnections(HttpExchange exchange) throws IOException, SQLException {
        if("GET".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                exchange.sendResponseHeaders(401, -1); // 401 unauthorized
                return;
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    try {
                        try {
                            response = DatabaseQueryController.selectConnectionList(userId).toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            exchange.sendResponseHeaders(400, -1); // 400 bad request
                            return;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        exchange.sendResponseHeaders(400, -1); // 400 bad request
                        return;
                    }
                }
                exchange.sendResponseHeaders(responseCode, response.length); // use the actual length of the response body
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 method not-allowed
            return;
        }
    }
    public static void watchPendingConnections(HttpExchange exchange) throws IOException, SQLException {
        if("GET".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                exchange.sendResponseHeaders(401, -1); // 401 unauthorized
                return;
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    //WatchPendingConnectionListRequest watchPendingConnectionListRequest = null;
                    try {
                        //watchPendingConnectionListRequest = gson.fromJson(reader, WatchPendingConnectionListRequest.class);
                        try {
                            response = DatabaseQueryController.selectPendingConnectionList(userId).toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            exchange.sendResponseHeaders(400, -1); // 400 bad request
                            return;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        exchange.sendResponseHeaders(400, -1); // 400 bad request
                        return;
                    }
                }
                exchange.sendResponseHeaders(responseCode, response.length); // use the actual length of the response body
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response);
                }
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // 405 method not-allowed
            return;
        }
    }
    public static void newPost(HttpExchange exchange) throws IOException, SQLException {
        if("POST".equalsIgnoreCase(exchange.getRequestMethod())) { // make other method equals check to equalsIgonreCase
            byte[] response;
            int responseCode;
            int postId = -1;

            Headers requestHeaders = exchange.getRequestHeaders();
            int userId = JwtHandler.validateSessionToken(requestHeaders);
            if(userId == -1) {
                response = UNAUTHORIZED.toByte("UTF-8");
                responseCode = UNAUTHORIZED.getStatusCode();
            }
            else {
                try (InputStream requestBody = exchange.getRequestBody();
                     InputStreamReader reader = new InputStreamReader(requestBody, "UTF-8")) {
                    Gson gson = new Gson();
                    Post post = null;
                    try {
                        post = gson.fromJson(reader, Post.class);
                        try {
                            postId = DatabaseQueryController.insertPost(post, userId);
                            response = SUCCESS.toByte("UTF-8");
                            responseCode = SUCCESS.getStatusCode();
                        } catch (SQLException exception) {
                            exception.printStackTrace();
                            response = INTERNAL_ERROR.toByte("UTF-8");
                            responseCode = INTERNAL_ERROR.getStatusCode();
                        }
                    }
                    catch (IllegalArgumentException e) {
                        logger.info(e.getMessage());
                        response = BAD_REQUEST.toByte("UTF-8");
                        responseCode = BAD_REQUEST.getStatusCode();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    response = BAD_REQUEST.toByte("UTF-8");
                    responseCode = BAD_REQUEST.getStatusCode();
                }
            }
            exchange.sendResponseHeaders(responseCode, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
            if (postId != -1) {
                List<Integer> connections = DatabaseQueryController.getConnectionList(userId);
                for (Integer connectionId : connections) {
                    DatabaseQueryController.addToWatchList(connectionId, postId);
                }
            }
        } else {
            byte[] response = METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(METHOD_NOT_ALLOWED.getStatusCode(), response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }        }
    }
}
