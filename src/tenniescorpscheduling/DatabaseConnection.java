/*
 * Class used to connect to the database.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String DB_NAME = "U05osu";
    private static final String DB_URL = "jdbc:mysql://52.206.157.109/" + DB_NAME;
    private static final String USERNAME = "U05osu";
    private static final String PASSWORD = "53688564723";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn;
    
    /*
    Makes the connection to the database
    @return the connetion to the database
    */
    public static Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        return conn;
    }
    
    /*
    Closes the connection to the database
    */
    public static void closeConnection() throws ClassNotFoundException, SQLException {
        conn.close();
    }

    public static Connection getConn() {
        return conn;
    }
}
