package Model;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileContactInfo {
    private String linkUrl; //TODO نمایش لینک نمایه کاربر
    private String emailAddress;
    private String phoneNumber;
    private PhoneType phoneType;
    private String address;
    private Date dateOfBirth;
    private ShowBirthDateTo showBirthDateTo;
    private String otherContactInfo; //telegram url or watsapp or ...
}
