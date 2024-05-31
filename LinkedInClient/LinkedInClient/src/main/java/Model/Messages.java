package Model;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public enum Messages {
    TAKEN_USERNAME("This username is already taken!", 0),
    INVALID_USERNAME("Username is invalid!", 1),
    INVALID_PASSWORD("Password is invalid!", 2),
    CONFIRMATION_PASSWORD("Confirmation does not match the password!", 3),
    INVALID_EMAIL("email is invalid!", 4),
    EMAIL_EXISTS("email already exists!", 5),
    INTERNAL_ERROR("Internal error!", 6),
    METHOD_NOT_ALLOWED("Method not allowed!", 7),
    INVALID_CREDENTIALS("Username or password is invalid!", 8),
    SUCCESS("success", 9);


    public final String message;
    public final int statusCode;

    private Messages(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public byte[] toByte(String charset) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString.getBytes(charset);
    }

    public static Messages fromByte(byte[] bytes) {
        try {
            String jsonString = new String(bytes, "UTF-8");
            Gson gson = new Gson();
            return gson.fromJson(jsonString, Messages.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return INTERNAL_ERROR; // default in case of error
        }
    }
}
