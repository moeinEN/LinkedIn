package Model;

public enum JobWorkplaceStatus {
    ON_SITE("On-site"),
    HYBRID("Hybrid"),
    REMOTE("Remote");

    private String value;
    private JobWorkplaceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
