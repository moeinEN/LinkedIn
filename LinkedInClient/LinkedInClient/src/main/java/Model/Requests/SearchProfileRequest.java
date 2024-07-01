package Model.Requests;

import Model.JobSkills;
import Model.JobStatus;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchProfileRequest {
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private JobStatus jobStatus;
    private String profession;
    private String jobTitle;
}
