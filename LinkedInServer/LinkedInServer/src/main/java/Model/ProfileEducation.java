package Model;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class ProfileEducation {
    private String instituteName;
    private Date educationStartDate;
    private Date educationEndDate;
    private Boolean stillOnEducation;
    private String GPA;
    private String descriptionOfActivitiesAndAssociations;
    private String description;
    private List<EducationalSkills> educationalSkills;
    private Boolean informOthersForTheProfileUpdate;
    private Boolean isCurrentProfileEducation;

    List<String> invalidFields = new ArrayList<>();


    public ProfileEducation(String instituteName, Date educationStartDate, Date educationEndDate, Boolean stillOnEducation, String GPA, String descriptionOfActivitiesAndAssociations, String description, List<EducationalSkills> educationalSkills, Boolean informOthersForTheProfileUpdate, Boolean isCurrentProfileEducation) {

        if (instituteName.length() <= 40) this.instituteName = instituteName;
        else invalidFields.add("instituteName");

        this.educationStartDate = educationStartDate;
        this.educationEndDate = educationEndDate;
        this.stillOnEducation = stillOnEducation;

        this.GPA = GPA;

        if (descriptionOfActivitiesAndAssociations.length() <= 500) this.descriptionOfActivitiesAndAssociations = descriptionOfActivitiesAndAssociations;
        else invalidFields.add("descriptionOfActivitiesAndAssociations");

        if (description.length() <= 1000) this.description = description;
        else invalidFields.add("description");

        if (educationalSkills.size() <= 5) this.educationalSkills = educationalSkills;
        else invalidFields.add("educationalSkills");

        this.informOthersForTheProfileUpdate = informOthersForTheProfileUpdate;

        this.isCurrentProfileEducation = isCurrentProfileEducation;

        if(!invalidFields.isEmpty()) {
            String invalidFieldString = "";
            for (String invalidField : invalidFields) {
                invalidFieldString = invalidField + "\\|";
            }
            throw new IllegalArgumentException(invalidFieldString);
        }
    }
}
