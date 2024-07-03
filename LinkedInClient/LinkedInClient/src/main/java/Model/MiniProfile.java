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
}
