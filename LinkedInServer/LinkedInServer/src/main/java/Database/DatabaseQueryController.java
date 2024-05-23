package Database;

import Controllers.SignUpController;
import Model.SignUpMessages;
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

    public static void createTableTokens() throws SQLException {
        String sql = "CREATE TABLE TOKENS (\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    token VARCHAR(255) UNIQUE,\n" +
                "    user_id INTEGER,\n" +
                "    expire_status VARCHAR(255),\n" +
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
            Connection db = null;
            Statement stmt = null;
            db = DbController.getConnection();
            db.setAutoCommit(true);
            stmt = db.createStatement();

            String usernameCheckSql = String.format("SELECT * FROM USER WHERE username = '%s'", username);
            ResultSet userRs = stmt.executeQuery(usernameCheckSql);
            if(userRs.next()) {
                return SignUpMessages.TAKEN_USERNAME.message;
            }

            String emailCheckSql = String.format("SELECT * FROM USER WHERE email = '%s'", email);
            ResultSet emailRs = stmt.executeQuery(emailCheckSql);
            if(emailRs.next()) {
                return SignUpMessages.EMAIL_EXISTS.message;
            }

            String sql = String.format("INSERT INTO USER (username, password, email) VALUES ('%s', '%s', '%s');", username, password, email);

            try {
                stmt.executeUpdate(sql);
                return "Successfully added user";
            } catch ( Exception e ) {
                e.printStackTrace();
                return "Internal server error";
            } finally {
                stmt.close();
                db.close();
            }
        } catch( Exception e ) {
            e.printStackTrace();
            return "Internal server error";
        }
    }
}
