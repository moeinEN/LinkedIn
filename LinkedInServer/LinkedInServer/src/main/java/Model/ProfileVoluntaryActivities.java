package Model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileVoluntaryActivities {
    private String desc;
    private Date date;
}
