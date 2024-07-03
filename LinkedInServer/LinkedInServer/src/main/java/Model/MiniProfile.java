package Model;

import com.google.gson.Gson;
import lombok.*;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import static Model.Messages.INTERNAL_ERROR;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MiniProfile {
    private String firstName;
    private String lastName;
    private String imageURL;
    private int identification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniProfile that = (MiniProfile) o;
        return identification == that.identification;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identification);
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
