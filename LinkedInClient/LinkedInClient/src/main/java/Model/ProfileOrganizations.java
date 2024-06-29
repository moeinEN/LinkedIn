package Model;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileOrganizations {
    private String organizationName;
    private String position;
    private Date startDate;
    private Date endDate;
    private boolean currentlyWorking;

}
