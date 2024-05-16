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
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;


    public byte[] toByte(String charset) throws UnsupportedEncodingException {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return jsonString.getBytes(charset);
    }
}