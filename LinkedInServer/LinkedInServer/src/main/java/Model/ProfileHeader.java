package Model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class ProfileHeader {
    private String firstName;
    private String lastName;
    private String additionalName;
    private String mainImageUrl;
    private String backgroundImageUrl;
    private String about;
    private ProfileJob currentJob;
    private ProfileEducation educationalInfo;
    private String country;
    private String city;
    private String profession;
    private ProfileContactInfo contactInfo;
    private String jobStatus;

    List<String> invalidFields = new ArrayList<>();

    public ProfileHeader(String firstName, String lastName, String additionalName, String mainImageUrl, String backgroundImageUrl, String about, ProfileJob currentJob, ProfileEducation educationalInfo, String country, String city, String profession, ProfileContactInfo contactInfo, String jobStatus) throws IllegalArgumentException{
        if(firstName.length() <= 20) this.firstName = firstName;
        else invalidFields.add("firstName");

        if(lastName.length() <= 40) this.lastName = lastName;
        else invalidFields.add("lastName");

        if(additionalName.length() <= 40) this.additionalName = additionalName;
        else invalidFields.add("additionalName");

        this.mainImageUrl = mainImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;

        if(about.length() <= 220) this.about = about;
        else invalidFields.add("about");

        this.currentJob = currentJob;
        this.educationalInfo = educationalInfo;

        if(country.length() <= 60) this.country = country;
        else invalidFields.add("country");

        if(city.length() <= 60) this.city = city;
        else invalidFields.add("city");

        if(profession.length() <= 60) this.profession = profession;
        else invalidFields.add("profession");

        this.contactInfo = contactInfo;
        this.jobStatus = jobStatus;

        if(!invalidFields.isEmpty()) {
            String invalidFieldString = "";
            for (String invalidField : invalidFields) {
                invalidField = invalidField + "\\|";
            }
            throw new IllegalArgumentException(invalidFieldString);
        }
    }
}
