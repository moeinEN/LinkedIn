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

    List<String> invalidFields = new ArrayList<>();

    public ProfileJob(Boolean informOthersForTheProfileUpdate, List<JobSkills> jobSkills, String description, Boolean currentlyWorking, Date endDate, Date startDate, Boolean companyActivityStatus, JobWorkplaceStatus jobWorkplaceStatus, String workplaceLocation, String companyName, JobStatus jobStatus, String title, Boolean isCurrentProfileJob) {
        this.informOthersForTheProfileUpdate = informOthersForTheProfileUpdate;

        if (jobSkills.size() <= 5) this.jobSkills = jobSkills;
        else invalidFields.add("jobSkills");

        if (description.length() <= 1000) this.description = description;
        else invalidFields.add("description");

        this.currentlyWorking = currentlyWorking;
        this.endDate = endDate;
        this.startDate = startDate;
        this.companyActivityStatus = companyActivityStatus;
        this.jobWorkplaceStatus = jobWorkplaceStatus;

        if (workplaceLocation.length() <= 40) this.workplaceLocation = workplaceLocation;
        else invalidFields.add("workplaceLocation");

        if (companyName.length() <= 40) this.companyName = companyName;
        else invalidFields.add("companyName");

        this.jobStatus = jobStatus;

        if (title.length() <= 40) this.title = title;
        else invalidFields.add("title");

        this.isCurrentProfileJob = isCurrentProfileJob;

        if(!invalidFields.isEmpty()) {
            String invalidFieldString = "";
            for (String invalidField : invalidFields) {
                invalidFieldString = invalidField + "\\|";
            }
            throw new IllegalArgumentException(invalidFieldString);
        }
    }
}
