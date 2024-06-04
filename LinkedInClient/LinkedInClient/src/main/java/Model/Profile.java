package Model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {
    @SerializedName("ProfileContactInfo")
    private ProfileContactInfo contactInfo;
    @SerializedName("ProfileEducation")
    private ProfileEducation education;
    @SerializedName("ProfileHeader")
    private ProfileHeader header;
}
