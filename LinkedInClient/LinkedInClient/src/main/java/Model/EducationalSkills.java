package Model;

public enum EducationalSkills {
    EMPTY("");
    private String value;
    private EducationalSkills(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
