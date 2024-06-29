package Model;

public enum PhoneType {
    MOBILE_PHONE("mobile-phone"),
    HOME_PHONE("home-phone"),
    WORK_PHONE("work-phone");


    private String value;
    private PhoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
