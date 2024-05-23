package Model;


import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {
    @SerializedName("user")
    User user;
    @SerializedName("name")
    String name;
    @SerializedName("familyName")
    String familyName;
    @SerializedName("birthDate")
    String birthDate;
}
