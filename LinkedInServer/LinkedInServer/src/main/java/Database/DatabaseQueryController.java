package Database;

import Model.*;

import java.sql.*;
import java.util.Objects;
import java.util.StringJoiner;

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
    public static void createTableComments() throws SQLException {
        String sql = "CREATE TABLE Comments (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    postId INTEGER,\n" +
                "    commenterId INTEGER,\n" +
                "    commentText TEXT,\n" +
                "    FOREIGN KEY (postId) REFERENCES Post(id),\n" +
                "    FOREIGN KEY (commenterId) REFERENCES USER(id)\n" + /*user or profile id??*/
                ");";
        createTable(sql);
    }
    public static void createTableLikes() throws SQLException {
        String sql = "CREATE TABLE Likes (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    postId INTEGER,\n" +
                "    likerId INTEGER,\n" +
                "    FOREIGN KEY (postId) REFERENCES Post(id),\n" +
                "    FOREIGN KEY (likerId) REFERENCES USER(id)\n" + /*user or profile id??*/
                ");";
        createTable(sql);
    }
    public static void createTablePosts() throws SQLException {
        String sql = "CREATE TABLE Posts (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    caption TEXT,\n" +
                "    identification INTEGER,\n" +
                "    FOREIGN KEY (identification) REFERENCES USER(id)\n" + /*user or profile id??*/
                ");";
        createTable(sql);
    }
    public static void createTableConnections() throws SQLException {
        String sql = "CREATE TABLE Connections (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    senderSpecifiedProfileId INTEGER,\n" +
                "    receiverSpecifiedProfileId INTEGER,\n" +
                "    connectionStatus TEXT,\n" +
                "    requestSentDate TEXT,\n" +
                "    FOREIGN KEY (senderSpecifiedProfileId) REFERENCES Profile(id),\n" +
                "    FOREIGN KEY (receiverSpecifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileSports() throws SQLException {
        String sql = "CREATE TABLE ProfileSports (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileExperienceId INTEGER,\n" +
                "    desc TEXT,\n" +
                "    date TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileExperienceId) REFERENCES ProfileExperience(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileVoluntaryActivities() throws SQLException {
        String sql = "CREATE TABLE ProfileVoluntaryActivities (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileExperienceId INTEGER,\n" +
                "    desc TEXT,\n" +
                "    date TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileExperienceId) REFERENCES ProfileExperience(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileExperience() throws SQLException {
        String sql = "CREATE TABLE ProfileExperience (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    militaryService TEXT,\n" +
                "    militaryServiceDate TEXT,\n" +
                "    ceoExperience TEXT,\n" +
                "    ceoExperienceDate TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileSkills() throws SQLException {
        String sql = "CREATE TABLE ProfileSkills (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    jobSkills TEXT,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableProfileOrganizations() throws SQLException {
        String sql = "CREATE TABLE ProfileOrganizations(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedProfileId INTEGER,\n" +
                "    organizationName TEXT,\n" +
                "    positionInOrganization TEXT,\n" +
                "    startCooperateDate TEXT,\n" +
                "    endCooperateDate TEXT,\n" +
                "    isActive INTEGER,\n" +
                "    FOREIGN KEY (specifiedProfileId) REFERENCES Profile(id)\n" +
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
                "    informOthersForTheProfileUpdate INTEGER,\n" +
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
                "    informOthersForTheProfileUpdate INTEGER,\n" +
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
                "    country TEXT,\n" +
                "    city TEXT,\n" +
                "    profession TEXT,\n" +
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
                "    relatedSkills INTEGER ,\n" +
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

        Connection conn = DbController.getConnection();
        if (Objects.isNull(conn)){
            throw new SQLException("Couldn't get connection");
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int profileId = generatedKeys.getInt(1);

                    ProfileExperience experience = profile.getProfileExperience();
                    for (ProfileJob job : experience.getJobs()) {
                        insertProfileJob(conn, job, profileId);
                    }
                    for (ProfileVoluntaryActivities activity : experience.getVoluntaryActivities()) {
                        insertProfileVoluntaryActivity(conn, activity, profileId);
                    }
                    insertProfileExperience(conn, experience, profileId);

                    for (ProfileEducation education : profile.getProfileEducationList()) {
                        insertProfileEducation(conn, education, profileId);
                    }
                    for (Certificate certificate : profile.getCertificatesList()) {
                        insertCertificate(conn, certificate, profileId);
                    }
                    insertProfileHeader(conn, profile.getHeader(), profileId);

                    insertProfileSkill(conn, profile.getSkills(), profileId);
                    insertOrganizationCooperate(conn, profile.getOrganizations(), profileId);
                }
                conn.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        }
    }

    public static void insertProfileHeader(Connection conn, ProfileHeader header, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileHeader (specifiedProfileId, firstName, lastName, additionalName, mainImageUrl, backgroundImageUrl, about, country, city, profession, jobStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int id;
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, header.getFirstName());
            pstmt.setString(3, header.getLastName());
            pstmt.setString(4, header.getAdditionalName());
            pstmt.setString(5, header.getMainImageUrl());
            pstmt.setString(6, header.getBackgroundImageUrl());
            pstmt.setString(7, header.getAbout());
            pstmt.setString(8, header.getCountry());
            pstmt.setString(9, header.getCity());
            pstmt.setString(10, header.getProfession());
            pstmt.setString(11, header.getJobStatus());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                id = generatedKeys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        insertProfileJob(conn,header.getCurrentJob(), id);
        insertProfileEducation(conn, header.getEducationalInfo(), id);
        insertProfileContactInfo(conn, header.getContactInfo(), id);
    }

    public static int insertProfileJob(Connection conn, ProfileJob job, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileJob (specifiedProfileId, title, jobStatus, companyName, workplaceLocation, jobWorkplaceStatus, companyActivityStatus, startDate, endDate, currentlyWorking, description, jobSkills, informOthersForTheProfileUpdate, isCurrentJob) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        StringJoiner joiner = new StringJoiner(",");
        for (JobSkills jobSkills: job.getJobSkills()){
            joiner.add(jobSkills.getValue());
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, job.getTitle());
            pstmt.setString(3, job.getJobStatus().getValue());
            pstmt.setString(4, job.getCompanyName());
            pstmt.setString(5, job.getWorkplaceLocation());
            pstmt.setString(6, job.getJobWorkplaceStatus().getValue());
            pstmt.setBoolean(7, job.getCompanyActivityStatus());
            pstmt.setString(8, job.getStartDate().toString());
            pstmt.setString(9, Objects.isNull(job.getEndDate()) ? null : job.getEndDate().toString());
            pstmt.setBoolean(10, job.getCurrentlyWorking());
            pstmt.setString(11, job.getDescription());
            pstmt.setString(12, joiner.toString());
            pstmt.setBoolean(13, job.getInformOthersForTheProfileUpdate());
            pstmt.setBoolean(14, job.getIsCurrentProfileJob());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    if (job.getIsCurrentProfileJob())
                        return generatedKeys.getInt(1);
                    else
                        return -1;
                } else {
                    throw new SQLException("Creating profile job failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static int insertProfileEducation(Connection conn, ProfileEducation education, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileEducation (specifiedProfileId, instituteName, educationStartDate, educationEndDate, stillOnEducation, GPA, descriptionOfActivitiesAndAssociations, description, educationalSkills, informOthersForTheProfileUpdate, isCurrentEducation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        StringJoiner joiner = new StringJoiner(",");
        for (EducationalSkills educationalSkills: education.getEducationalSkills()){
            joiner.add(educationalSkills.getValue());
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, education.getInstituteName());
            pstmt.setString(3, education.getEducationStartDate().toString());
            pstmt.setString(4, education.getEducationEndDate().toString());
            pstmt.setBoolean(5, education.getStillOnEducation());
            pstmt.setString(6, education.getGPA());
            pstmt.setString(7, education.getDescriptionOfActivitiesAndAssociations());
            pstmt.setString(8, education.getDescription());
            pstmt.setString(9, joiner.toString());
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
                    throw new SQLException("Creating profile education failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void insertCertificate(Connection conn, Certificate certificate, int profileId) throws SQLException {
        String sql = "INSERT INTO Certificate (specifiedProfileId, name, organizationName, issueDate, expiryDate, certificateId, certificateURL, relatedSkills) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        StringJoiner joiner = new StringJoiner(",");
        for (String relatedSkills: certificate.getRelatedSkills()){
            joiner.add(relatedSkills);
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, certificate.getName());
            pstmt.setString(3, certificate.getOrganizationName());
            pstmt.setString(4, certificate.getIssueDate().toString());
            pstmt.setString(5, certificate.getExpiryDate().toString());
            pstmt.setString(6, certificate.getCertificateId());
            pstmt.setString(7, certificate.getCertificateURL());
            pstmt.setString(8, joiner.toString());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating certificate failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static int insertProfileContactInfo(Connection conn, ProfileContactInfo contactInfo, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileContactInfo (specifiedProfileHeaderId, linkUrl, emailAddress, phoneNumber, phoneType, address, dateOfBirth, showBirthDateTo, otherContactInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, contactInfo.getLinkUrl());
            pstmt.setString(3, contactInfo.getEmailAddress());
            pstmt.setString(4, contactInfo.getPhoneNumber());
            pstmt.setString(5, contactInfo.getPhoneType().getValue());
            pstmt.setString(6, contactInfo.getAddress());
            pstmt.setString(7, contactInfo.getDateOfBirth().toString());
            pstmt.setString(8, contactInfo.getShowBirthDateTo().toString());
            pstmt.setString(9, contactInfo.getOtherContactInfo());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating profile contact info failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertComment(int postId, int commenterId, String commentText) throws SQLException {
        String sql = "INSERT INTO Comments (postId, commenterId, commentText) VALUES (?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, commenterId);
            pstmt.setString(3, commentText);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertLike(int postId, int likerId) throws SQLException {
        String sql = "INSERT INTO Likes (postId, likerId) VALUES (?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, postId);
            pstmt.setInt(2, likerId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertPost(String caption, int userId) throws SQLException {
        String sql = "INSERT INTO Posts (caption, identification) VALUES (?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(true);
            pstmt.setString(1, caption);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertConnection(int senderProfileId, int receiverProfileId, String connectionStatus, String requestSentDate) throws SQLException {
        String sql = "INSERT INTO Connections (senderSpecifiedProfileId, receiverSpecifiedProfileId, connectionStatus, requestSentDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(true);
            pstmt.setInt(1, senderProfileId);
            pstmt.setInt(2, receiverProfileId);
            pstmt.setString(3, connectionStatus);
            pstmt.setString(4, requestSentDate);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertProfileExperience(Connection conn, ProfileExperience experience, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileExperience (specifiedProfileId, militaryService, militaryServiceDate, ceoExperience, ceoExperienceDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, experience.getMilitaryService());
            pstmt.setString(3, experience.getMilitaryServiceDate().toString());
            pstmt.setString(4, experience.getCeoExperience());
            pstmt.setString(5, experience.getCeoExperienceDate().toString());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating profile experience failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void insertProfileSkill(Connection conn, ProfileSkills skill, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileSkills (specifiedProfileId, jobSkills) VALUES (?, ?)";
        StringJoiner joiner = new StringJoiner(",");
        for (JobSkills jobSkills: skill.getJobSkills()){
            joiner.add(jobSkills.getValue());
        }
        String joinedString = joiner.toString();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, joiner.toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void insertOrganizationCooperate(Connection conn, ProfileOrganizations org, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileOrganizations (specifiedProfileId, organizationName, positionInOrganization, startCooperateDate, endCooperateDate, isActive) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, org.getOrganizationName());
            pstmt.setString(3, org.getPosition());
            pstmt.setString(4, org.getStartDate().toString());
            pstmt.setString(5, Objects.isNull(org.getEndDate()) ? null : org.getEndDate().toString());
            pstmt.setInt(6, org.getCurrentlyWorking() ? 1 : 0);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void insertProfileVoluntaryActivity(Connection conn, ProfileVoluntaryActivities activity, int experienceId) throws SQLException {
        String sql = "INSERT INTO ProfileVoluntaryActivities (specifiedProfileExperienceId, desc, date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, experienceId);
            pstmt.setString(2, activity.getDesc());
            pstmt.setString(3, activity.getDate().toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
