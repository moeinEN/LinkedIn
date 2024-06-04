package Model;

public enum JobStatus {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    SELF_EMPLOYED("Self-employed"),
    FREELANCE("Freelance"),
    CONTRACT("Contract"),
    INTERNSHIP("Internship"),
    PAID_INTERNSHIP("Paid Internship"),
    SEASONAL("Seasonal");

    private String value;
    private JobStatus(String value) {
        this.value = value;
    }
}
