package Model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginCredentials {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
}
