package Model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileExperience {
    private List<ProfileJob> jobs;
    private List<ProfileVoluntaryActivities> voluntaryActivities;
    private String militaryService;
    private Date militaryServiceDate;
    private String ceoExperience;
    private Date ceoExperienceDate;
    private List<ProfileSports> profileSports;
}
