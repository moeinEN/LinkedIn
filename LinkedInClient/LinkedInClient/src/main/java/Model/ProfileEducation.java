package Model;

import lombok.*;

import java.util.Date;
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
    private boolean stillOnEducation;
    private String GPA;
    private String descriptionOfActivitiesAndAssociations;
    private String description;
    private List<EducationalSkills> educationalSkills;
    private boolean informOthersForTheProfileUpdate;
    private boolean isCurrentProfileEducation;
}
