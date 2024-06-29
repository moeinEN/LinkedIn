package Model;

public enum JobSkills {
    EMPTY("");
    private String value;
    private JobSkills(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
