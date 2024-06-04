package Controller;

import Database.DatabaseQueryController;
import Model.LoginCredentials;
import Model.Messages;
import Model.RegisterCredentials;
import Model.User;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
            byte[] response = Messages.METHOD_NOT_ALLOWED.toByte("UTF-8");
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
            byte[] response = Messages.METHOD_NOT_ALLOWED.toByte("UTF-8");
            exchange.sendResponseHeaders(405, response.length); // 405 Method Not Allowed
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
    }
}
