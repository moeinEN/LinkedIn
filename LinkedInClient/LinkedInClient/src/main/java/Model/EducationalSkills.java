package Model;

public enum EducationalSkills {
    EMPTY(""),
    MATHEMATICS("Mathematics"),
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    BIOLOGY("Biology"),
    COMPUTER_SCIENCE("Computer Science"),
    SOFTWARE_ENGINEERING("Software Engineering"),
    DATA_SCIENCE("Data Science"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    MACHINE_LEARNING("Machine Learning"),
    WEB_DEVELOPMENT("Web Development"),
    MOBILE_DEVELOPMENT("Mobile Development"),
    DATABASE_MANAGEMENT("Database Management"),
    NETWORKING("Networking"),
    CYBER_SECURITY("Cyber Security"),
    CLOUD_COMPUTING("Cloud Computing"),
    BUSINESS_ADMINISTRATION("Business Administration"),
    MARKETING("Marketing"),
    FINANCE("Finance"),
    ACCOUNTING("Accounting"),
    ECONOMICS("Economics"),
    PSYCHOLOGY("Psychology"),
    SOCIOLOGY("Sociology"),
    EDUCATION("Education"),
    LITERATURE("Literature"),
    HISTORY("History"),
    PHILOSOPHY("Philosophy"),
    POLITICAL_SCIENCE("Political Science"),
    INTERNATIONAL_RELATIONS("International Relations"),
    LAW("Law"),
    MEDICINE("Medicine"),
    NURSING("Nursing"),
    PHARMACY("Pharmacy"),
    PUBLIC_HEALTH("Public Health"),
    ENVIRONMENTAL_SCIENCE("Environmental Science"),
    ENGINEERING("Engineering"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    CIVIL_ENGINEERING("Civil Engineering"),
    AEROSPACE_ENGINEERING("Aerospace Engineering"),
    ARCHITECTURE("Architecture"),
    DESIGN("Design"),
    GRAPHIC_DESIGN("Graphic Design"),
    FINE_ARTS("Fine Arts"),
    MUSIC("Music"),
    THEATRE("Theatre"),
    COMMUNICATION("Communication"),
    JOURNALISM("Journalism"),
    MEDIA_STUDIES("Media Studies"),
    LANGUAGES("Languages"),
    FOREIGN_LANGUAGES("Foreign Languages");

    private String value;

    private EducationalSkills(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
