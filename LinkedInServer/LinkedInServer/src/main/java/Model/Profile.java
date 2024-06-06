package Model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {
    @SerializedName("ProfileExperiences")
    private ProfileExperience profileExperience;
    @SerializedName("ProfileEducationList")
    private List<ProfileEducation> profileEducationList;
    @SerializedName("CertificatesList")
    private List<Certificate> certificatesList;
    @SerializedName("ProfileHeader")
    private ProfileHeader header;
    @SerializedName("ProfileSkills")
    private ProfileSkills skills;
    @SerializedName("ProfileOrganizations")
    private ProfileOrganizations organizations;
}
