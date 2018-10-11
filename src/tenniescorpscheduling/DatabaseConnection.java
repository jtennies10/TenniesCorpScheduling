/*
 * Class used to connect to the database.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
*Class for connecting to the database
*/
public class DatabaseConnection {
    private static final String dbName = "U05osu";
    private static final String dbURL = "jdbc:mysql://52.206.157.109/" + dbName;
    private static final String username = "U05osu";
    private static final String password = "53688564723";
    private static final String driver = "com.mysql.jdbc.Driver";
    static Connection conn;
    
    public static Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        conn = DriverManager.getConnection(dbURL, username, password);
        System.out.println("Connection successful!");
        return conn;
    }
    
    public static void closeConnection() throws ClassNotFoundException, SQLException {
        conn.close();
        System.out.println("Connection closed!");
    }
    
    public static Connection getConn() {
        return conn;
    }
}
