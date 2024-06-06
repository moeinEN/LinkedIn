package Database;

import Model.*;

import java.sql.*;
import java.util.Objects;

public class DatabaseQueryController {
    //TODO rollback option


    private static void createTable (String script) throws SQLException {
        Connection db = null;
        Statement stmt = null;
        db = DbController.getConnection();
        db.setAutoCommit(true);
        stmt = db.createStatement();
        try {
            stmt.execute(script);
            stmt.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }
    public static void createTableUsers() throws SQLException {
        String sql = "CREATE TABLE USER (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    username VARCHAR(255) UNIQUE,\n" +
                "    password VARCHAR(255),\n" +
                "    email VARCHAR(255) UNIQUE\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileJob() throws SQLException {
        String sql = "CREATE TABLE ProfileJob (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    title TEXT,\n" +
                "    jobStatus TEXT,\n" +
                "    companyName TEXT,\n" +
                "    workplaceLocation TEXT,\n" +
                "    jobWorkplaceStatus TEXT,\n" +
                "    companyActivityStatus INTEGER,\n" +
                "    startDate TEXT,\n" +
                "    endDate TEXT,\n" +
                "    currentlyWorking INTEGER,\n" +
                "    description TEXT,\n" +
                "    jobSkills TEXT,\n" +
                "    informOthersForTheProfileUpdate INTEGER\n" +
                "    isCurrentJob INTEGER,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileEducation() throws SQLException {
        String sql = "CREATE TABLE ProfileEducation (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    instituteName TEXT,\n" +
                "    educationStartDate TEXT,\n" +
                "    educationEndDate TEXT,\n" +
                "    stillOnEducation INTEGER,\n" +
                "    GPA TEXT,\n" +
                "    descriptionOfActivitiesAndAssociations TEXT,\n" +
                "    description TEXT,\n" +
                "    educationalSkills TEXT,\n" +
                "    informOthersForTheProfileUpdate INTEGER\n" +
                "    isCurrentEducation INTEGER,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileContactsInfo() throws SQLException {
        String sql = "CREATE TABLE ProfileContactInfo (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileHeaderId INTEGER,\n" +
                "    linkUrl TEXT,\n" +
                "    emailAddress TEXT,\n" +
                "    phoneNumber TEXT,\n" +
                "    phoneType TEXT,\n" +
                "    address TEXT,\n" +
                "    dateOfBirth TEXT,\n" +
                "    showBirthDateTo TEXT,\n" +
                "    otherContactInfo TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileHeaderId) REFERENCES ProfileHeader(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileHeader() throws SQLException {
        String sql = "CREATE TABLE ProfileHeader (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    firstName TEXT,\n" +
                "    lastName TEXT,\n" +
                "    additionalName TEXT,\n" +
                "    mainImageUrl TEXT,\n" +
                "    backgroundImageUrl TEXT,\n" +
                "    about TEXT,\n" +
                "    currentJobId INTEGER,\n" +
                "    educationalInfoId INTEGER,\n" +
                "    country TEXT,\n" +
                "    city TEXT,\n" +
                "    profession TEXT,\n" +
                "    contactInfoId INTEGER,\n" +
                "    jobStatus TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableCertificate() throws SQLException {
        String sql = "CREATE TABLE Certificate (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    name TEXT,\n" +
                "    organizationName TEXT,\n" +
                "    issueDate TEXT,\n" +
                "    expiryDate TEXT,\n" +
                "    certificateId TEXT,\n" +
                "    certificateURL TEXT,\n" +
                "    relatedSkills INTEGER , -- Enum values stored as integers\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfile() throws SQLException {
        String sql = "CREATE TABLE Profile (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    userId INTEGER,\n" +
                "    FOREIGN KEY (userId) REFERENCES USER(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableTokens() throws SQLException {
        String sql = "CREATE TABLE TOKENS (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    token VARCHAR(255) UNIQUE,\n" +
                "    user_id INTEGER,\n" +
                "    expire_status VARCHAR(255)\n" +
                ");";
        createTable(sql);
    }
    public static User getUser(String username) throws SQLException {
        String sql = String.format("SELECT * FROM USER WHERE username = '%s';", username);
        Connection db = null;
        Statement stmt = null;
        db = DbController.getConnection();
        db.setAutoCommit(true);
        stmt = db.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (Objects.isNull(rs)) {
                return null;
            }
            User user = new User();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
            }
            return user;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
        finally {
            stmt.close();
            db.close();
        }
    }
    public static int getUserId(String username) throws SQLException {
        String sql = String.format("SELECT * FROM USER WHERE username = '%s';", username);
        Connection db = null;
        Statement stmt = null;
        db = DbController.getConnection();
        db.setAutoCommit(true);
        stmt = db.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (Objects.isNull(rs)) {
                return -1;
            }
            int userId = 0;
            while (rs.next()) {
                userId = rs.getInt("id");
            }
            return userId;
        } catch ( Exception e ) {
            e.printStackTrace();
            return -1;
        }
        finally {
            stmt.close();
            db.close();
        }
    }
    public static Messages addUser(String username, String password, String email) {
        try {
            Connection db = null;
            Statement stmt = null;
            db = DbController.getConnection();
            db.setAutoCommit(true);
            stmt = db.createStatement();

            String usernameCheckSql = String.format("SELECT * FROM USER WHERE username = '%s'", username);
            ResultSet userRs = stmt.executeQuery(usernameCheckSql);
            if(userRs.next()) {
                return Messages.TAKEN_USERNAME;
            }

            String emailCheckSql = String.format("SELECT * FROM USER WHERE email = '%s'", email);
            ResultSet emailRs = stmt.executeQuery(emailCheckSql);
            if(emailRs.next()) {
                return Messages.EMAIL_EXISTS;
            }

            String sql = String.format("INSERT INTO USER (username, password, email) VALUES ('%s', '%s', '%s');", username, password, email);

            try {
                stmt.executeUpdate(sql);
                return Messages.SUCCESS;
            } catch ( Exception e ) {
                e.printStackTrace();
                return Messages.INTERNAL_ERROR;
            } finally {
                stmt.close();
                db.close();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
    }
    public static Messages checkCredentials(LoginCredentials loginCredentials) {
        try {
            Connection db = null;
            Statement stmt = null;
            db = DbController.getConnection();
            db.setAutoCommit(true);
            stmt = db.createStatement();

            // search for valid login credentials
            String usernameCheckSql = String.format("SELECT * FROM USER WHERE username = '%s' AND password = '%s'", loginCredentials.getUsername(), loginCredentials.getPassword());
            try {
                ResultSet userRs = stmt.executeQuery(usernameCheckSql);
                if(!userRs.next()) {
                    return Messages.INVALID_CREDENTIALS;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return Messages.INTERNAL_ERROR;
            }
            finally {
                stmt.close();
                db.close();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
        return Messages.SUCCESS;
    }
    public static Messages CheckJwtCredentials(String username, String password, String email) {
        try {
            Connection db = null;
            Statement stmt = null;
            db = DbController.getConnection();
            db.setAutoCommit(true);
            stmt = db.createStatement();

            // search for valid login credentials
            String usernameCheckSql = String.format("SELECT * FROM USER WHERE username = '%s' AND password = '%s' AND email = '%s'", username, password, email);
            try {
                ResultSet userRs = stmt.executeQuery(usernameCheckSql);
                if(!userRs.next()) {
                    return Messages.INVALID_CREDENTIALS;
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return Messages.INTERNAL_ERROR;
            }
            finally {
                stmt.close();
                db.close();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            return Messages.INTERNAL_ERROR;
        }
        return Messages.SUCCESS;
    }
//    public static Messages addProfileToDatabase(Profile profile, int userId) {
//
//    }
    public static void insertProfile(Profile profile, int userId) throws SQLException {
        String sql = "INSERT INTO Profile (userId) VALUES (?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

            int currentProfileJobId = -1;
            int currentProfileEducationId = -1;
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int profileId = generatedKeys.getInt(1);

                    for (ProfileJob job : profile.getProfileJobList()) {
                        int returnedId = insertProfileJob(job, profileId);;
                        if (returnedId != -1) currentProfileJobId = returnedId;
                    }
                    for (ProfileEducation education : profile.getProfileEducationList()) {
                        int returnedId = insertProfileEducation(education, profileId);
                        if (returnedId != -1) currentProfileEducationId = returnedId;
                    }
                    for (Certificate certificate : profile.getCertificatesList()) {
                        insertCertificate(certificate, profileId);
                    }
                    int contactInfoId = insertProfileContactInfo(profile.getHeader().getContactInfo(), profileId);
                    insertProfileHeader(profile.getHeader(), profileId, contactInfoId, currentProfileJobId, currentProfileJobId);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    public static void insertProfileHeader(ProfileHeader header, int profileId, int contactInfoId, int currentProfileJobId, int currentProfileEducationId) throws SQLException {
        String sql = "INSERT INTO ProfileHeader (specifiedProfileId, firstName, lastName, additionalName, mainImageUrl, backgroundImageUrl, about, currentJobId, educationalInfoId, country, city, profession, contactInfoId, jobStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, header.getFirstName());
            pstmt.setString(3, header.getLastName());
            pstmt.setString(4, header.getAdditionalName());
            pstmt.setString(5, header.getMainImageUrl());
            pstmt.setString(6, header.getBackgroundImageUrl());
            pstmt.setString(7, header.getAbout());
            pstmt.setInt(8, currentProfileJobId);
            pstmt.setInt(9, currentProfileEducationId);
            pstmt.setString(10, header.getCountry());
            pstmt.setString(11, header.getCity());
            pstmt.setString(12, header.getProfession());
            pstmt.setInt(13, contactInfoId);
            pstmt.setString(14, header.getJobStatus());
            pstmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    public static int insertProfileJob(ProfileJob job, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileJob (specifiedProfileId, title, jobStatus, companyName, workplaceLocation, jobWorkplaceStatus, companyActivityStatus, startDate, endDate, currentlyWorking, description, jobSkills, informOthersForTheProfileUpdate, isCurrentJob) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, job.getTitle());
            pstmt.setString(3, job.getJobStatus().getValue());
            pstmt.setString(4, job.getCompanyName());
            pstmt.setString(5, job.getWorkplaceLocation());
            pstmt.setString(6, job.getJobWorkplaceStatus().getValue());
            pstmt.setBoolean(7, job.getCompanyActivityStatus());
            pstmt.setString(8, job.getStartDate().toString());
            pstmt.setString(9, job.getEndDate().toString());
            pstmt.setBoolean(10, job.getCurrentlyWorking());
            pstmt.setString(11, job.getDescription());
            pstmt.setString(12, String.join((CharSequence) ",", (CharSequence) job.getJobSkills()));
            pstmt.setBoolean(13, job.getInformOthersForTheProfileUpdate());
            pstmt.setBoolean(14, job.getIsCurrentProfileJob());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    if(job.getIsCurrentProfileJob())
                        return generatedKeys.getInt(1);
                    else
                        return -1;
                } else {
                    throw new SQLException("Creating profile job failed, no ID obtained.");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static int insertProfileEducation(ProfileEducation education, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileEducation (specifiedProfileId, instituteName, educationStartDate, educationEndDate, stillOnEducation, GPA, descriptionOfActivitiesAndAssociations, description, educationalSkills, informOthersForTheProfileUpdate, isCurrentEducation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, education.getInstituteName());
            pstmt.setString(3, education.getEducationStartDate().toString());
            pstmt.setString(4, education.getEducationEndDate().toString());
            pstmt.setBoolean(5, education.getStillOnEducation());
            pstmt.setString(6, education.getGPA());
            pstmt.setString(7, education.getDescriptionOfActivitiesAndAssociations());
            pstmt.setString(8, education.getDescription());
            pstmt.setString(9, String.join((CharSequence) ",", (CharSequence) education.getEducationalSkills()));
            pstmt.setBoolean(10, education.getInformOthersForTheProfileUpdate());
            pstmt.setBoolean(11, education.getIsCurrentProfileEducation());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    if (education.getIsCurrentProfileEducation())
                        return generatedKeys.getInt(1);
                    else
                        return -1;
                } else {
                    throw new SQLException("Creating profile job failed, no ID obtained.");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void insertCertificate(Certificate certificate, int profileId) throws SQLException {
        String sql = "INSERT INTO Certificate (specifiedProfileId, name, organizationName, issueDate, expiryDate, certificateId, certificateURL, relatedSkills) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, certificate.getName());
            pstmt.setString(3, certificate.getOrganizationName());
            pstmt.setString(4, certificate.getIssueDate().toString());
            pstmt.setString(5, certificate.getExpiryDate().toString());
            pstmt.setString(6, certificate.getCertificateId());
            pstmt.setString(7, certificate.getCertificateURL());
            pstmt.setString(8, String.join(",", certificate.getRelatedSkills()));
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating profile job failed, no ID obtained.");
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int insertProfileContactInfo(ProfileContactInfo contactInfo, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileContactInfo (specifiedProfileHeaderId, linkUrl, emailAddress, phoneNumber, phoneType, address, dateOfBirth, showBirthDateTo, otherContactInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, contactInfo.getLinkUrl());
            pstmt.setString(3, contactInfo.getEmailAddress());
            pstmt.setString(4, contactInfo.getPhoneNumber());
            pstmt.setString(5, contactInfo.getPhoneType().toString());
            pstmt.setString(6, contactInfo.getAddress());
            pstmt.setString(7, contactInfo.getDateOfBirth().toString());
            pstmt.setString(8, contactInfo.getShowBirthDateTo().toString());
            pstmt.setString(9, contactInfo.getOtherContactInfo());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // return the id of the inserted profile job
                } else {
                    throw new SQLException("Creating profile job failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
