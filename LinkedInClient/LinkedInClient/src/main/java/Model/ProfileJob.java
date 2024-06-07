package Model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileJob {
    private String title;
    private JobStatus jobStatus;
    private String companyName;
    private String workplaceLocation;
    private JobWorkplaceStatus jobWorkplaceStatus;
    private Boolean companyActivityStatus; //TODO وضعیت فعالیت در شرکت
    private Date startDate;
    private Date endDate;
    private Boolean currentlyWorking;
    private String description;
    private List<JobSkills> jobSkills;
    private Boolean informOthersForTheProfileUpdate;
    private Boolean isCurrentProfileJob; //true for profile header
}

