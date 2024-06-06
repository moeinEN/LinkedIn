package Model;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileSkills {
    private List<JobSkills> jobSkills;
    private List<EducationalSkills> educationalSkillsList;
}
