package Model;

import java.sql.Date;

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
