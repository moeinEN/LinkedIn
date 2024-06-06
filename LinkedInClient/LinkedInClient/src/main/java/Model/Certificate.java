package Model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class    Certificate {
    private String name;
    private String organizationName;
    private Date issueDate;
    private Date expiryDate;
    private String certificateId;
    private String certificateURL;
    private List<String> relatedSkills;
}
