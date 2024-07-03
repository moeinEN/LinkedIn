package Model;

import com.google.gson.Gson;
import lombok.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static Model.Messages.INTERNAL_ERROR;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentIndividual {
    private HashMap<Integer, String> comments = new HashMap<>();
    private MiniProfile miniProfile;

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
