package Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Profile {
    private ProfileContactInfo contactInfo;
    private ProfileEducation education;
    private ProfileHeader header;
}
