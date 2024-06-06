package Model.Requests;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterCredentials {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("confirmationPassword")
    private String confirmationPassword;
    @SerializedName("username")
    private String username;
}
