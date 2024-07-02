package Model.Response;

import Model.*;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static Model.Messages.INTERNAL_ERROR;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WatchProfileResponse {
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
    @SerializedName("ProfileIdentificationCode")
    private int identificationCode;
    @SerializedName("ProfileFeed")
    private Feed feed;

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
