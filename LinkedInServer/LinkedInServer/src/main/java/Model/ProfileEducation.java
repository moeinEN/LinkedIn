package Model;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
