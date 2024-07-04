package Database;

import Model.*;
import Model.Requests.*;
import Model.Response.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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
            db.setAutoCommit(false);
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
                db.commit();
                return Messages.SUCCESS;
            } catch ( Exception e ) {
                e.printStackTrace();
                db.rollback();
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


    //POST
    public static void createTableUserWatchList() throws SQLException {
        String sql = "CREATE TABLE UserWatchList (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedUserId INTEGER,\n" +
                "    specifiedPostId INTEGER,\n" +
                "    FOREIGN KEY (specifiedUserId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedPostId) REFERENCES POST(id)\n" +
                ");";
        createTable(sql);
    }
    public static void addToWatchList(int userId, int postId) throws SQLException {
        String sql = "INSERT INTO UserWatchList (specifiedUserId, specifiedPostId) VALUES (?, ?)";
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //TODO recheck getWatchList Method
    public static List<Integer> getWatchList(int userId) throws SQLException {
        String sql = "SELECT specifiedPostId FROM UserWatchList WHERE specifiedUserId = ?";
        List<Integer> watchList = new ArrayList<>();
        try (Connection conn = DbController.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                watchList.add(rs.getInt("specifiedPostId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchList;
    }
    public static void createTablePost() throws SQLException {
        String sql = "CREATE TABLE POST (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedUserId INTEGER,\n" +
                "    caption TEXT,\n" +
                "    hashtag TEXT,\n" +
                "    mediaName TEXT,\n" +
                "    FOREIGN KEY (specifiedUserId) REFERENCES USER(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableComment() throws SQLException {
        String sql = "CREATE TABLE Comment (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedUserId INTEGER,\n" +
                "    specifiedPostId INTEGER,\n" +
                "    comment TEXT,\n" +
                "    FOREIGN KEY (specifiedUserId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedPostId) REFERENCES POST(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableLike() throws SQLException {
        String sql = "CREATE TABLE Like (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedUserId INTEGER,\n" +
                "    specifiedPostId INTEGER,\n" +
                "    FOREIGN KEY (specifiedUserId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedPostId) REFERENCES POST(id)\n" +
                ");";
        createTable(sql);
    }
    public static int insertPost(Post post, int userId) throws SQLException {
        String sql = "INSERT INTO POST (specifiedUserId, caption, hashtag, mediaName) VALUES (?, ?, ?, ?)";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int generatedId = -1;
        try {
            pstmt.setInt(1, userId);
            pstmt.setString(2, post.getText());
            StringJoiner joiner = new StringJoiner(",");
            for (String str : post.getHashtags()) {
                joiner.add(str);
            }
            pstmt.setString(3, joiner.toString());
            pstmt.setString(4, post.getMediaName());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }
            ProfileHeader profileHeaderSender = getProfileHeader(conn, getProfileIdFromUserId(conn, userId));
            String msg = "Your Connection " + profileHeaderSender.getFirstName() + " " + profileHeaderSender.getLastName() + " has posted something";
            insertPostNotification(conn, userId, msg);
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            pstmt.close();
            conn.close();
        }
        return generatedId;
    }
    public static void insertComment(CommentRequest comment, int userId) throws SQLException {
        String sql = "INSERT INTO COMMENT (specifiedUserId, specifiedPostId, comment) VALUES (?, ?, ?)";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        try {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, comment.getPostIdentification());
            pstmt.setString(3, comment.getComment());
            pstmt.executeUpdate();
            conn.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertLike(LikeRequest likeRequest, int userId) throws SQLException {
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        if (!likeRequest.getLikeOrDislike()) {
            deleteLikeIfExist(conn, likeRequest.getPostIdentification(), userId);
        } else {
            String sql = "INSERT INTO LIKE (specifiedUserId, specifiedPostId) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            try {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, likeRequest.getPostIdentification());
                pstmt.executeUpdate();
                conn.commit();
            }
            catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
            }
        }
    }
    public static void deleteLikeIfExist(Connection conn, int postID, int userID) throws SQLException {
        String sql = "DELETE FROM LIKE WHERE specifiedUserId = ? AND specifiedPostId = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        try {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, postID);
            pstmt.executeUpdate();
            conn.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
        }
    }
    public static Like getLikes(Connection conn, int specifiedPostId) throws SQLException {
        String sql = "SELECT * FROM Like WHERE specifiedPostId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, specifiedPostId);
            ResultSet rs = pstmt.executeQuery();
            Like like = new Like();
            while (rs.next()) {
                int userId = rs.getInt("specifiedUserId");
                int profileId = getProfileIdFromUserId(conn, userId);
                MiniProfile miniProfile = getUserMiniProfile(conn, profileId);
                like.getLikedUsers().add(miniProfile);
            }
            return like;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static Comment getComments(Connection conn, int specifiedPostId) throws SQLException {
        String sql = "SELECT * FROM Comment WHERE specifiedPostId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, specifiedPostId);
            ResultSet rs = pstmt.executeQuery();
            Comment comment = new Comment();
            while (rs.next()) {
                int userId = rs.getInt("specifiedUserId");
                int commentId = rs.getInt("id");
                String commentText = rs.getString("comment");
                int profileId = getProfileIdFromUserId(conn, userId);
                MiniProfile miniProfile = getUserMiniProfile(conn, profileId);
                if (comment.getCommentedUsers().containsKey(profileId)){
                    comment.getCommentedUsers().get(profileId).getComments().put(commentId, commentText);
                } else {
                    CommentIndividual commentIndividual = new CommentIndividual();
                    commentIndividual.getComments().put(commentId, commentText);
                    commentIndividual.setMiniProfile(miniProfile);
                    comment.getCommentedUsers().put(profileId, commentIndividual);
                }
            }
            return comment;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static WatchPostSearchResults getPostBySearch(SearchPostsRequest searchPostsRequest) throws SQLException {
        String sql = "SELECT * FROM POST WHERE caption like ? OR hashtag like ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        String searchedText = "%" + searchPostsRequest.getText() + "%";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchedText);
            pstmt.setString(2, searchedText);

            ResultSet rs = pstmt.executeQuery();
            WatchPostSearchResults watchPostSearchResults = new WatchPostSearchResults();
            while (rs.next()) {
                int postId = rs.getInt("id");
                String caption = rs.getString("caption");
                String[] hashtags = rs.getString("hashtag").split(",");
                List<String> hashtagList = new ArrayList<>(Arrays.asList(hashtags));
                Like like = getLikes(conn, postId);
                Comment comment = getComments(conn, postId);
                String mediaName = rs.getString("mediaName");
                Post post = new Post();
                post.setComments(comment);
                post.setText(caption);
                post.setLikes(like);
                post.setIdentification(postId);
                post.setHashtags(hashtagList);
                post.setMediaName(mediaName);
                watchPostSearchResults.getPosts().add(post);
            }
            conn.commit();
            return watchPostSearchResults;
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
        }
        return new WatchPostSearchResults();
    }
    //CONNECTION/FOLLOW
    public static void createTableConnect() throws SQLException {
        String sql = "CREATE TABLE Connect (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedSenderId INTEGER,\n" +
                "    specifiedReceiverId INTEGER,\n" +
                "    FOREIGN KEY (specifiedSenderId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedReceiverId) REFERENCES USER(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTableFollow() throws SQLException {
        String sql = "CREATE TABLE Follow (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedfollowerId INTEGER,\n" +
                "    specifiedfollowingId INTEGER,\n" +
                "    FOREIGN KEY (specifiedfollowerId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedfollowingId) REFERENCES USER(id)\n" +
                ");";
        createTable(sql);
    }
    public static void createTablePendingConnect() throws SQLException {
        String sql = "CREATE TABLE Pending (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedSenderId INTEGER,\n" +
                "    specifiedReceiverId INTEGER,\n" +
                "    note TEXT,\n" +
                "    FOREIGN KEY (specifiedSenderId) REFERENCES USER(id),\n" +
                "    FOREIGN KEY (specifiedReceiverId) REFERENCES USER(id)\n" +
                ");";
        createTable(sql);
    }
    public static void insertPendingConnect(int senderId, ConnectRequest connectRequest) throws SQLException {
        String sql = "INSERT INTO Pending (specifiedSenderId, specifiedReceiverId, note) VALUES (?, ?, ?)";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        try {
            int receiverId = getUserIdFromProfileId(conn, connectRequest.getIdentificationCode());
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.setString(3, connectRequest.getNote());
            pstmt.executeUpdate();
            conn.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        }
    }
    public static List<Integer> getConnectionList(int receiverId) throws SQLException {
        String sql1 = "SELECT * FROM Connect WHERE specifiedReceiverId = ?";
        String sql2 = "SELECT * FROM Connect WHERE specifiedSenderId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        List<Integer> connectionList = new ArrayList<>();
        try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt1.setInt(1, receiverId);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                int senderId = rs1.getInt("specifiedSenderId");
                connectionList.add(senderId);
            }

            pstmt2.setInt(1, receiverId);
            ResultSet rs2 = pstmt2.executeQuery();
            while (rs2.next()) {
                int receiverId1 = rs2.getInt("specifiedReceiverId");
                connectionList.add(receiverId1);
            }
            conn.commit();
            return connectionList;
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            return new ArrayList<>();
        } finally {
            conn.setAutoCommit(true);
        }
    }
    public static WatchConnectionPendingLists selectPendingConnectionList(int receiverId) throws SQLException {
        String sql = "SELECT * FROM Pending WHERE specifiedReceiverId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, receiverId);
            ResultSet rs = pstmt.executeQuery();
            WatchConnectionPendingLists watchConnectionPendingLists = new WatchConnectionPendingLists();
            while (rs.next()) {
                int senderId = rs.getInt("specifiedSenderId");
                int profileId = getProfileIdFromUserId(conn, senderId);
                String note = rs.getString("note");
                MiniProfile miniProfile = getUserMiniProfile(conn, profileId);
                if (Objects.nonNull(miniProfile)) {
                    ConnectionPendingObject connectionPendingObject = new ConnectionPendingObject(miniProfile, note);
                    watchConnectionPendingLists.getPendingLists().put(profileId, connectionPendingObject);
                }
            }
            conn.commit();
            return watchConnectionPendingLists;
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
        }
        return new WatchConnectionPendingLists();
    }
    public static WatchConnectionListResponse selectConnectionList(int receiverId) throws SQLException {
        String sql1 = "SELECT * FROM Connect WHERE specifiedReceiverId = ?";
        String sql2 = "SELECT * FROM Connect WHERE specifiedSenderId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        WatchConnectionListResponse watchConnectionListResponse = new WatchConnectionListResponse();
        try (PreparedStatement pstmt1 = conn.prepareStatement(sql1);
             PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt1.setInt(1, receiverId);
            ResultSet rs1 = pstmt1.executeQuery();
            while (rs1.next()) {
                int senderId = rs1.getInt("specifiedSenderId");
                int senderProfileId = getProfileIdFromUserId(conn, senderId);
                MiniProfile miniProfile = getUserMiniProfile(conn, senderProfileId);
                if (Objects.nonNull(miniProfile)) {
                    watchConnectionListResponse.getConnectionList().add(miniProfile);
                }
            }

            pstmt2.setInt(1, receiverId);
            ResultSet rs2 = pstmt2.executeQuery();
            while (rs2.next()) {
                int senderId = rs2.getInt("specifiedReceiverId");
                int senderProfileId = getProfileIdFromUserId(conn, senderId);
                MiniProfile miniProfile = getUserMiniProfile(conn, senderProfileId);
                if (Objects.nonNull(miniProfile)) {
                    watchConnectionListResponse.getConnectionList().add(miniProfile);
                }
            }

            conn.commit();
            return watchConnectionListResponse;
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            return new WatchConnectionListResponse();
        } finally {
            conn.setAutoCommit(true);
        }
    }
    public static void acceptOrDeclineConnection(int receiverId, AcceptConnection acceptConnection) throws SQLException {
        String sql = "DELETE FROM Pending WHERE specifiedSenderId = ? AND specifiedReceiverId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int senderId = acceptConnection.getConnectionIdentification();
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
            if (acceptConnection.getAcceptOrDecline()){
                addConnectionToUser(conn, senderId, receiverId);
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        }
    }
    public static void addConnectionToUser(Connection conn, int senderId, int receiverId) throws SQLException {
        String sql = "INSERT INTO Connect (specifiedSenderId, specifiedReceiverId) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
            ProfileHeader profileHeaderSender = getProfileHeader(conn, getProfileIdFromUserId(conn, senderId));
            ProfileHeader profileHeaderReceiver = getProfileHeader(conn, getProfileIdFromUserId(conn, receiverId));
            String msg = profileHeaderSender.getFirstName() + " " + profileHeaderSender.getLastName() +
                    " is now connected with" + " " + profileHeaderReceiver.getFirstName() + " " + profileHeaderReceiver.getLastName();
            insertConnectorFollowNotification(conn, senderId, receiverId, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void addFollowToUser(int senderId, FollowRequest followRequest) throws SQLException {
        String sql = "INSERT INTO Follow (specifiedSenderId, specifiedReceiverId) VALUES (?, ?)";
        Connection conn = DbController.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int receiverId = followRequest.getIdentificationCode();
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
            ProfileHeader profileHeaderSender = getProfileHeader(conn, getProfileIdFromUserId(conn, senderId));
            ProfileHeader profileHeaderReceiver = getProfileHeader(conn, getProfileIdFromUserId(conn, receiverId));
            String msg = profileHeaderSender.getFirstName() + " " + profileHeaderSender.getLastName() +
                    " is now following " + " " + profileHeaderReceiver.getFirstName() + " " + profileHeaderReceiver.getLastName();
            insertConnectorFollowNotification(conn, senderId, receiverId, msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //CREATE PROFILE TABLES
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
                "    educationalSkills TEXT,\n" +
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
                "    relatedSkills TEXT,\n" +
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

    //INSERT PROFILE
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
        finally {
            conn.close();
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
                if (generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                    insertProfileJob(conn,header.getCurrentJob(), id);
                    insertProfileEducation(conn, header.getEducationalInfo(), id);
                    insertProfileContactInfo(conn, header.getContactInfo(), id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertProfileJob(Connection conn, ProfileJob job, int profileId) throws SQLException {
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
                    int profileExperienceId = generatedKeys.getInt(1);
                    for (ProfileJob job : experience.getJobs()) {
                        insertProfileJob(conn, job, profileId);
                    }
                    for (ProfileVoluntaryActivities activity : experience.getVoluntaryActivities()) {
                        insertProfileVoluntaryActivity(conn, activity, profileExperienceId);
                    }

                    for (ProfileSports profileSports : experience.getProfileSports()) {
                        insertProfileSports(conn, profileSports, profileExperienceId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertProfileSkill(Connection conn, ProfileSkills skill, int profileId) throws SQLException {
        String sql = "INSERT INTO ProfileSkills (specifiedProfileId, jobSkills, educationalSkills) VALUES (?, ?, ?)";
        StringJoiner joiner = new StringJoiner(",");
        for (JobSkills jobSkills: skill.getJobSkills()){
            joiner.add(jobSkills.getValue());
        }
        StringJoiner joiner1 = new StringJoiner(",");
        for (EducationalSkills educationalSkills : skill.getEducationalSkillsList()){
            joiner1.add(educationalSkills.getValue());
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, profileId);
            pstmt.setString(2, joiner.toString());
            pstmt.setString(3, joiner1.toString());
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
    public static void insertProfileSports(Connection conn, ProfileSports sports, int experienceId) throws SQLException {
        String sql = "INSERT INTO ProfileSports (specifiedProfileExperienceId, desc, date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //conn.setAutoCommit(true);
            pstmt.setInt(1, experienceId);
            pstmt.setString(2, sports.getDesc());
            pstmt.setString(3, sports.getDate().toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    //SELECT PROFILE
    public static MiniProfile getUserMiniProfile(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM ProfileHeader WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String imageUrl = rs.getString("mainImageUrl");
            return new MiniProfile(firstName, lastName, imageUrl, profileId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static WatchProfileSearchResults getWatchProfileSearchResults(SearchProfileRequest searchProfileRequest) throws SQLException {
        String sql = "SELECT * FROM (SELECT ProfileHeader.specifiedProfileId, ProfileHeader.firstName, ProfileHeader.lastName, ProfileHeader.city, ProfileHeader.country, ProfileHeader.jobStatus, ProfileHeader.profession, ProfileJob.title, ProfileJob.isCurrentJob FROM ProfileHeader INNER JOIN ProfileJob ON ProfileHeader.specifiedProfileId = ProfileJob.specifiedProfileId) WHERE firstName LIKE ? AND lastName LIKE ? AND city LIKE ? AND country LIKE ? AND jobStatus LIKE ? AND profession LIKE ? AND title LIKE ? AND isCurrentJob = ?;";
        Connection conn = DbController.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchProfileRequest.getFirstName() + "%");
            pstmt.setString(2, "%" + searchProfileRequest.getLastName() + "%");
            pstmt.setString(3, "%" + searchProfileRequest.getCity() + "%");
            pstmt.setString(4, "%" + searchProfileRequest.getCountry() + "%");
            pstmt.setString(5, "%" + searchProfileRequest.getJobStatus().getValue() + "%");
            pstmt.setString(6, "%" + searchProfileRequest.getProfession() + "%");
            pstmt.setString(7, "%" + searchProfileRequest.getJobTitle() + "%");
            pstmt.setInt(8, 1);
            ResultSet rs = pstmt.executeQuery();

            List<MiniProfile> miniProfiles = new ArrayList<>();
            while (rs.next()) {
                miniProfiles.add(getUserMiniProfile(conn, rs.getInt("specifiedProfileId")));
            }
            return new WatchProfileSearchResults(miniProfiles);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return null;
    }

    public static WatchProfileResponse getWatchProfileRequest(WatchProfileRequest watchProfileRequest) throws SQLException {
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        ProfileExperience profileExperience = getProfileExperience(conn, watchProfileRequest.getProfileId());
        List<ProfileEducation> profileEducations = getProfileEducation(conn, watchProfileRequest.getProfileId(), false);
        List<Certificate> certificates = getCertificate(conn, watchProfileRequest.getProfileId());
        ProfileHeader profileHeader = getProfileHeader(conn, watchProfileRequest.getProfileId());
        ProfileSkills profileSkills = getProfileSkills(conn, watchProfileRequest.getProfileId());
        ProfileOrganizations profileOrganizations = getProfileOrganization(conn, watchProfileRequest.getProfileId());
        int userId = getUserIdFromProfileId(conn, watchProfileRequest.getProfileId());
        Feed feed = getFeed(userId);
        WatchProfileResponse profileToWatch = new WatchProfileResponse(profileExperience, profileEducations, certificates, profileHeader, profileSkills, profileOrganizations, watchProfileRequest.getProfileId(), feed);
        conn.close();
        return profileToWatch;
    }
    public static ProfileExperience getProfileExperience(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM ProfileExperience WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();
            List<ProfileJob> profileJobs = getProfileJob(conn, rs.getInt("specifiedProfileId"), false);
            List<ProfileVoluntaryActivities> profileVoluntaryActivities = getProfileVoluntaryActivities(conn, rs.getInt("id"));
            String militaryService = rs.getString("militaryServiceDate");
            Date militaryServiceDate = Date.valueOf(rs.getString("militaryServiceDate"));
            String ceoExperience = rs.getString("ceoExperience");
            Date ceoExperienceDate = Date.valueOf(rs.getString("ceoExperienceDate"));
            List<ProfileSports> profileSports = getProfileSports(conn, rs.getInt("id"));
            return new ProfileExperience(profileJobs, profileVoluntaryActivities, militaryService, militaryServiceDate, ceoExperience, ceoExperienceDate, profileSports);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<ProfileJob> getProfileJob(Connection conn, int profileId, boolean isCurrent) throws SQLException {
        String sql = "SELECT * FROM ProfileJob WHERE specifiedProfileId = ? AND isCurrentJob = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            if(isCurrent)
                pstmt.setInt(2, 1);
            else
                pstmt.setInt(2, 0);

            ResultSet rs = pstmt.executeQuery();

            List<ProfileJob> profileJobs = new ArrayList<>();
            while (rs.next()) {
                String title = rs.getString("title");
                JobStatus jobStatus = JobStatus.valueOf(rs.getString("jobStatus"));
                String companyName = rs.getString("companyName");
                String workplaceLocation = rs.getString("workplaceLocation");
                JobWorkplaceStatus jobWorkplaceStatus = JobWorkplaceStatus.valueOf(rs.getString("jobWorkplaceStatus"));
                Boolean companyActivityStatus = rs.getBoolean("companyActivityStatus");
                Date startDate = Date.valueOf(rs.getString("startDate"));
                Date endDate = Date.valueOf(rs.getString("endDate"));
                Boolean currentlyWorking = rs.getBoolean("currentlyWorking");
                String description = rs.getString("description");
                List<JobSkills> jobSkills = new ArrayList<>();
                String[] skills = rs.getString("jobSkills").split(",");
                for (String str : skills)
                    jobSkills.add(JobSkills.valueOf(str));
                Boolean informOthersForTheProfileUpdate = rs.getBoolean("informOthersForTheProfileUpdate");
                Boolean isCurrentJob = rs.getBoolean("isCurrentJob");
                profileJobs.add(new ProfileJob(title, jobStatus, companyName, workplaceLocation, jobWorkplaceStatus, companyActivityStatus, startDate, endDate, currentlyWorking, description, jobSkills, informOthersForTheProfileUpdate, isCurrentJob));
            }
            return profileJobs;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<ProfileVoluntaryActivities> getProfileVoluntaryActivities(Connection conn, int profileExperienceId) throws SQLException{
        String sql = "SELECT * FROM ProfileVoluntaryActivities WHERE specifiedProfileExperienceId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, profileExperienceId);
            ResultSet rs = pstmt.executeQuery();

            List<ProfileVoluntaryActivities> profileVoluntaryActivities = new ArrayList<>();
            while (rs.next()) {
                String desc = rs.getString("desc");
                Date date = Date.valueOf(rs.getString("date"));
                profileVoluntaryActivities.add(new ProfileVoluntaryActivities(desc, date));
            }
            return profileVoluntaryActivities;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static List<ProfileSports> getProfileSports(Connection conn, int profileExperienceId) throws SQLException {
        String sql = "SELECT * FROM ProfileSports WHERE specifiedProfileExperienceId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileExperienceId);
            ResultSet rs = pstmt.executeQuery();

            List<ProfileSports> profileSports = new ArrayList<>();
            while (rs.next()) {
                String desc = rs.getString("desc");
                Date date = Date.valueOf(rs.getString("date"));
                profileSports.add(new ProfileSports(desc, date));
            }
            return profileSports;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<ProfileEducation> getProfileEducation(Connection conn, int profileId, boolean isCurrent) throws SQLException {
        String sql = "SELECT * FROM ProfileEducation WHERE specifiedProfileId = ? AND isCurrentEducation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            if (isCurrent)
                pstmt.setInt(2, 1);
            else
                pstmt.setInt(2, 1);

            ResultSet rs = pstmt.executeQuery();

            List<ProfileEducation> profileEducations = new ArrayList<>();
            while (rs.next()) {
                String instituteName = rs.getString("instituteName");
                Date educationStartDate = Date.valueOf(rs.getString("educationStartDate"));
                Date educationEndDate = Date.valueOf(rs.getString("educationEndDate"));
                Boolean stillOnEducation = rs.getBoolean("stillOnEducation");
                String GPA = rs.getString("GPA");
                String descriptionOfActivitiesAndAssociations = rs.getString("descriptionOfActivitiesAndAssociations");
                String description = rs.getString("description");
                List<EducationalSkills> educationalSkills = new ArrayList<>();
                String[] skills = rs.getString("educationalSkills").split(",");
                for (String str : skills)
                    educationalSkills.add(EducationalSkills.valueOf(str));
                Boolean informOthersForTheProfileUpdate = rs.getBoolean("informOthersForTheProfileUpdate");
                Boolean isCurrentEducation = rs.getBoolean("isCurrentEducation");
                profileEducations.add(new ProfileEducation(instituteName, educationStartDate, educationEndDate, stillOnEducation, GPA, descriptionOfActivitiesAndAssociations, description, educationalSkills, informOthersForTheProfileUpdate, isCurrentEducation));
            }
            return profileEducations;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Certificate> getCertificate(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM Certificate WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();

            List<Certificate> certificates = new ArrayList<>();
            while (rs.next()) {
                String name = rs.getString("name");
                String organizationName = rs.getString("organizationName");
                Date issueDate = Date.valueOf(rs.getString("issueDate"));
                Date expiryDate = Date.valueOf(rs.getString("expiryDate"));
                String certificateId = rs.getString("certificateId");
                String certificateURL = rs.getString("certificateURL");
                List<String> relatedSkills = new ArrayList<>();
                String[] skills  = rs.getString("relatedSkills").split(",");
                for (String str : skills)
                    relatedSkills.add(str);

                certificates.add(new Certificate(name, organizationName, issueDate, expiryDate, certificateId, certificateURL, relatedSkills));
            }
            return certificates;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ProfileHeader getProfileHeader(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM ProfileHeader WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();

            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String additionalName = rs.getString("additionalName");
            String mainImageUrl = rs.getString("mainImageUrl");
            String backgroundImageUrl = rs.getString("backgroundImageUrl");
            String about = rs.getString("about");
            ProfileJob profileJob = getProfileJob(conn, profileId, true).get(0);
            ProfileEducation profileEducation = getProfileEducation(conn, profileId, true).get(0);
            String country = rs.getString("country");
            String city = rs.getString("city");
            String profession = rs.getString("profession");
            String jobStatus = rs.getString("jobStatus");
            ProfileContactInfo profileContactInfo = getProfileContactInfo(conn, rs.getInt("id"));
            return new ProfileHeader(firstName, lastName, additionalName, mainImageUrl, backgroundImageUrl, about, profileJob, profileEducation, country, city, profession, profileContactInfo, jobStatus);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ProfileContactInfo getProfileContactInfo(Connection conn, int profileHeaderId) throws SQLException {
        String sql = "SELECT * FROM ProfileContactInfo WHERE specifiedProfileHeaderId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileHeaderId);
            ResultSet rs = pstmt.executeQuery();

            String linkUrl = rs.getString("linkUrl");
            String emailAddress = rs.getString("emailAddress");
            String phoneNumber = rs.getString("phoneNumber");
            PhoneType phoneType = PhoneType.valueOf(rs.getString("phoneType"));
            String address = rs.getString("address");
            Date dateOfBirth = Date.valueOf(rs.getString("dateOfBirth"));
            ShowBirthDateTo showBirthDateTo = ShowBirthDateTo.valueOf(rs.getString("showBirthDateTo"));
            String otherContactInfo = rs.getString("otherContactInfo");

            return new ProfileContactInfo(linkUrl, emailAddress, phoneNumber, phoneType, address, dateOfBirth, showBirthDateTo, otherContactInfo);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ProfileSkills getProfileSkills(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM ProfileSkills WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();

            List<JobSkills> jobSkills = new ArrayList<>();
            String[] skills = rs.getString("jobSkills").split(",");
            for (String str : skills){
                jobSkills.add(JobSkills.valueOf(str));
            }

            List<EducationalSkills> educationalSkills = new ArrayList<>();
            String[] eSkills = rs.getString("educationalSkills").split(",");
            for (String str : eSkills){
                educationalSkills.add(EducationalSkills.valueOf(str));
            }

            return new ProfileSkills(jobSkills, educationalSkills);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ProfileOrganizations getProfileOrganization(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM ProfileOrganizations WHERE specifiedProfileId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();

            String organizationName = rs.getString("organizationName");
            String positionInOrganization = rs.getString("positionInOrganization");
            Date startCooperateDate = Date.valueOf(rs.getString("startCooperateDate"));
            Date endCooperateDate = Date.valueOf(rs.getString("endCooperateDate"));
            Boolean isActive = rs.getBoolean("isActive");

            return new ProfileOrganizations(organizationName, positionInOrganization, startCooperateDate, endCooperateDate, isActive);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static int getUserIdFromProfileId(Connection conn, int profileId) throws SQLException {
        String sql = "SELECT * FROM Profile WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profileId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("userId");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }
    public static int getProfileIdFromUserId(Connection conn, int userId) throws SQLException {
        String sql = "SELECT * FROM Profile WHERE userId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }
    public static Feed getFeed(int userId) throws SQLException {
        String sql = "SELECT * FROM POST WHERE userId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            Feed feed = new Feed();
            while (rs.next()) {
                Post post = new Post();
                int postId = rs.getInt("id");
                String caption = rs.getString("caption");
                String[] hashtags = rs.getString("hashtag").split(",");
                List<String> hashtagList = new ArrayList<>(Arrays.asList(hashtags));
                Like like = getLikes(conn, postId);
                Comment comment = getComments(conn, postId);
                post.setComments(comment);
                post.setText(caption);
                post.setLikes(like);
                post.setIdentification(postId);
                post.setHashtags(hashtagList);
                feed.getPosts().add(post);
            }
            conn.commit();
            return feed;
        }
        catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            throw e;
        }
    }




    //NOTIFICATION
    public static void createTableNotification() throws SQLException {
        String sql = "CREATE TABLE NOTIFICATION (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    specifiedUserId INTEGER,\n" +
                "    notification TEXT,\n" +
                "    FOREIGN KEY (specifiedUserId) REFERENCES USER(id),\n" +
                ");";
        createTable(sql);
    }
    public static void insertPostNotification(Connection conn, int userId, String notification) throws SQLException {
        Set<Integer> connectionIDs = new HashSet<>();
        getAllConnectionAndFollowersId(userId, connectionIDs);
        try {
            for (Integer connectionID : connectionIDs) {
                insertSingleNotification(conn, connectionID, notification);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertConnectorFollowNotification(Connection conn, int userIdSender, int userIdReceiver, String notification) throws SQLException {
        Set<Integer> connectionIDs = new HashSet<>();
        getAllConnectionAndFollowersId(userIdSender, connectionIDs);
        getAllConnectionAndFollowersId(userIdReceiver, connectionIDs);
        try {
            for (Integer connectionID : connectionIDs) {
                insertSingleNotification(conn, connectionID, notification);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertSingleNotification(Connection conn, int userId, String notification) throws SQLException {
        String sql = "INSERT INTO Notification (specifiedUserId, notification) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, notification);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getAllConnectionAndFollowersId(int userId, Set<Integer> set) throws SQLException {
        String sql1 = "SELECT * FROM Connect WHERE specifiedSenderId = ?";
        String sql2 = "SELECT * FROM Connect WHERE specifiedReceiverId = ?";
        String sql3 = "SELECT * FROM Follow WHERE specifiedfollowerId = ?";
        String sql4 = "SELECT * FROM Follow WHERE specifiedfollowingId = ?";
        Connection conn = DbController.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getInt("specifiedReceiverId"));
            }
        }  catch (Exception e1) {
            e1.printStackTrace();
            throw e1;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql2)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getInt("specifiedSenderId"));
            }
        }  catch (Exception e2) {
            e2.printStackTrace();
            throw e2;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql3)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getInt("specifiedfollowingId"));
            }
        }  catch (Exception e3) {
            e3.printStackTrace();
            throw e3;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql4)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                set.add(rs.getInt("specifiedfollowerId"));
            }
        }  catch (Exception e4) {
            e4.printStackTrace();
            throw e4;
        }
    }





}