package Model;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public enum Messages {
    TAKEN_USERNAME("This username is already taken!"),
    INVALID_USERNAME("Username is invalid!"),
    INVALID_PASSWORD("Password is invalid!"),
    CONFIRMATION_PASSWORD("Confirmation does not match the password!"),
    INVALID_EMAIL("email is invalid!"),
    EMAIL_EXISTS("email already exists!"),
    INTERNAL_ERROR("Internal error!"),
    METHOD_NOT_ALLOWED("Method not allowed!"),
    SUCCESS("success");


    public final String message;

    private Messages(String message) {
        this.message = message;
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
