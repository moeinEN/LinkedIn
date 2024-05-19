package Model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.UnsupportedEncodingException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("familyName")
    private String familyName;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("email")
    private String email;


    public byte[] toByte(String charset) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString.getBytes(charset);
    }
}