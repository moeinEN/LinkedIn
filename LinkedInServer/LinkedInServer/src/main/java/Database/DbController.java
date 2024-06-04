package Database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbController {

    private static Logger logger = Logger.getLogger(DbController.class.getName());

    public static Connection getConnection() {
        Connection db = null;
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:src/main/resources/BackEndDb.db");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        //System.out.println("Connected to database");

        return db;
    }
    public static void closeConnection(Connection db) throws SQLException {
        db.close();
    }
    public static Statement getStatement(Connection db) {
        Statement stmt = null;

        try {
            stmt = db.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.log(Level.INFO, "Statement created");
        return stmt;
    }
    public static void closeStatement(Statement stmt) throws SQLException {
        stmt.close();
    }
}
