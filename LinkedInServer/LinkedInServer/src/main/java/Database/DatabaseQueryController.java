package Database;

import Model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueryController {
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
                "    name VARCHAR(255),\n" +
                "    familyName VARCHAR(255),\n" +
                "    username VARCHAR(255) UNIQUE,\n" +
                "    password VARCHAR(255),\n" +
                "    email VARCHAR(255) UNIQUE\n" +
                ");";
        createTable(sql);
    }
    public static User getUser(String username) throws SQLException {
        String sql = "SELECT * FROM USER WHERE username = '" + username + "';";
        Connection db = null;
        Statement stmt = null;
        db = DbController.getConnection();
        db.setAutoCommit(true);
        stmt = db.createStatement();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.wasNull()) {
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
    public static String addUser(String username, String password, String email) {
        try {
            String sql = String.format("INSERT INTO USER (username, password, email) VALUES ('%s', '%s', '%s');", username, password, email);
            Connection db = null;
            Statement stmt = null;
            db = DbController.getConnection();
            db.setAutoCommit(true);
            stmt = db.createStatement();
            try {
                stmt.executeUpdate(sql);
                return "Successfully added user";
            } catch ( Exception e ) {
                e.printStackTrace();
                return "Insertion failed";
            } finally {
                stmt.close();
                db.close();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            return "Insertion failed";
        }
    }
}
