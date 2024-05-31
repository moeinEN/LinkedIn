package Model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrganizationCooperate {
    private String organizationName;
    private String positionInOrganization;
    private Date startCooperateDate;
    private Date endCooperateDate;
    private boolean isActive;
}
