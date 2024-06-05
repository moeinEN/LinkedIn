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
public class ProfileContactInfo {
    private String linkUrl; //TODO نمایش لینک نمایه کاربر
    private String emailAddress;
    private String phoneNumber;
    private PhoneType phoneType;
    private String address;
    private Date dateOfBirth;
    private ShowBirthDateTo showBirthDateTo;
    private String otherContactInfo; //telegram url or watsapp or ...

    List<String> invalidFields = new ArrayList<>();

    public ProfileContactInfo(String otherContactInfo, ShowBirthDateTo showBirthDateTo, Date dateOfBirth, String address, PhoneType phoneType, String phoneNumber, String emailAddress, String linkUrl) {
        this.otherContactInfo = otherContactInfo;
        this.showBirthDateTo = showBirthDateTo;
        this.dateOfBirth = dateOfBirth;

        if (address.length() <= 220) this.address = address;
        else invalidFields.add("address");

        this.phoneType = phoneType;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.linkUrl = linkUrl;

        if(!invalidFields.isEmpty()) {
            String invalidFieldString = "";
            for (String invalidField : invalidFields) {
                invalidFieldString = invalidField + "\\|";
            }
            throw new IllegalArgumentException(invalidFieldString);
        }
    }
}
