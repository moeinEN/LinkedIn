package Model;

public enum JobSkills {
    EMPTY(""),
    PROJECT_MANAGEMENT("Project Management"),
    TEAM_LEADERSHIP("Team Leadership"),
    TIME_MANAGEMENT("Time Management"),
    COMMUNICATION("Communication"),
    PUBLIC_SPEAKING("Public Speaking"),
    NEGOTIATION("Negotiation"),
    SALES("Sales"),
    MARKETING("Marketing"),
    DIGITAL_MARKETING("Digital Marketing"),
    CONTENT_CREATION("Content Creation"),
    SOCIAL_MEDIA_MANAGEMENT("Social Media Management"),
    CUSTOMER_SERVICE("Customer Service"),
    BUSINESS_ANALYSIS("Business Analysis"),
    DATA_ANALYSIS("Data Analysis"),
    FINANCIAL_ANALYSIS("Financial Analysis"),
    SOFTWARE_DEVELOPMENT("Software Development"),
    WEB_DEVELOPMENT("Web Development"),
    MOBILE_APP_DEVELOPMENT("Mobile App Development"),
    DATABASE_MANAGEMENT("Database Management"),
    NETWORK_SECURITY("Network Security"),
    CYBER_SECURITY("Cyber Security"),
    CLOUD_COMPUTING("Cloud Computing"),
    ARTIFICIAL_INTELLIGENCE("Artificial Intelligence"),
    MACHINE_LEARNING("Machine Learning"),
    DEVOPS("DevOps"),
    QUALITY_ASSURANCE("Quality Assurance"),
    PRODUCT_MANAGEMENT("Product Management"),
    SUPPLY_CHAIN_MANAGEMENT("Supply Chain Management"),
    HUMAN_RESOURCES("Human Resources"),
    RECRUITMENT("Recruitment"),
    TRAINING_AND_DEVELOPMENT("Training and Development"),
    PERFORMANCE_MANAGEMENT("Performance Management"),
    CHANGE_MANAGEMENT("Change Management"),
    STRATEGIC_PLANNING("Strategic Planning"),
    OPERATIONS_MANAGEMENT("Operations Management"),
    RISK_MANAGEMENT("Risk Management"),
    CONSULTING("Consulting"),
    GRAPHIC_DESIGN("Graphic Design"),
    UX_DESIGN("UX Design"),
    UI_DESIGN("UI Design"),
    VIDEO_PRODUCTION("Video Production"),
    COPYWRITING("Copywriting"),
    EDITING("Editing"),
    TRANSLATION("Translation"),
    LEGAL_RESEARCH("Legal Research"),
    CONTRACT_MANAGEMENT("Contract Management"),
    EVENT_PLANNING("Event Planning"),
    HOSPITALITY_MANAGEMENT("Hospitality Management"),
    HEALTHCARE_MANAGEMENT("Healthcare Management"),
    TEACHING("Teaching"),
    RESEARCH("Research"),
    LABORATORY_SKILLS("Laboratory Skills"),
    ENGINEERING_DESIGN("Engineering Design"),
    CAD("CAD"),
    ELECTRICAL_ENGINEERING("Electrical Engineering"),
    MECHANICAL_ENGINEERING("Mechanical Engineering"),
    CIVIL_ENGINEERING("Civil Engineering"),
    ARCHITECTURE("Architecture"),
    CONSTRUCTION_MANAGEMENT("Construction Management");

    private String value;

    private JobSkills(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
