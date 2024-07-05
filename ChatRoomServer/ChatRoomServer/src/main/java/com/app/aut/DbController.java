package com.app.aut;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class DbController {
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
    public static Connection getConnection() {
        Connection db = null;
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:src/main/resources/ChatDb.db");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        //System.out.println("Connected to database");
        return db;
    }
    public static void createTableChats() throws SQLException {
        String sql = "CREATE TABLE CHATS (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    secret VARCHAR(255) NOT NULL,\n" +
                "    userId Integer NOT NULL,\n" +
                "    date TEXT,\n" +
                "    chat VARCHAR(255) \n" +
                ");";
        createTable(sql);
    }
    public static List<Message> getAllChats(String secret){
        String sql = "SELECT * FROM CHATS where secret = ? ORDER BY date";
        Connection conn = DbController.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Message> messages = new CopyOnWriteArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, secret);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                int userId = rs.getInt("userId");
                String chat = rs.getString("chat");
                Message message = new Message(String.valueOf(userId), chat, date);
                messages.add(message);
            }
            return messages;
        }catch ( Exception e ) {
            e.printStackTrace();
            return messages;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void insertSingleChat(Connection conn, String secret, Message message) throws SQLException {
        String sql = "INSERT INTO CHATS (secret, userId, date, chat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, secret);
            pstmt.setInt(2, Integer.parseInt(message.getSender()));
            pstmt.setString(3, message.getTimestamp().toString());
            pstmt.setString(4, message.getContent());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static void insertAllChats(String secret, List<Message> messages) {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LocalDateTime newest = getNewerTime(secret);
        boolean checkNewest = Objects.nonNull(newest);
        try {
            for (Message message : messages) {
                if (checkNewest) {
                    if (message.getTimestamp().isAfter(newest)) {
                        insertSingleChat(conn, secret, message);
                    }
                } else {
                    insertSingleChat(conn, secret, message);
                }
            }
            conn.commit();
        } catch (Exception e){
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static LocalDateTime getNewerTime(String secret) {
        String sql = "SELECT MAX(date) AS date FROM CHATS where secret = ?";
        Connection conn = DbController.getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, secret);
            ResultSet rs = pstmt.executeQuery();
            if (rs != null && rs.next()) {
               String date = rs.getString("date");
                if (Objects.nonNull(date)){
                    return LocalDateTime.parse(date);
                }
               return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

