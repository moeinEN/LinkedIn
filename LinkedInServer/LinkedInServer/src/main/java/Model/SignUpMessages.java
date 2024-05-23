package Model;

public enum SignUpMessages {
    TAKEN_USERNAME("This username is already taken!"),
    INVALID_USERNAME("Username is invalid!"),
    INVALID_PASSWORD("Password is invalid!"),
    CONFIRMATION_PASSWORD("Confirmation does not match the password!"),
    INVALID_EMAIL("email is invalid!"),
    EMAIL_EXISTS("email already exists!"),
    SUCCESS("success");


    public final String message;

    private SignUpMessages(String message) {
        this.message = message;
    }
}
