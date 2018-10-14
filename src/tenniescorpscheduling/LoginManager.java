/*
 * Class used to interact with data from the database with help from the 
 * DatabaseConnection class.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginManager {
    
    static Scanner sc = new Scanner(System.in);
    //private constructor so the class is not instantiated
    private LoginManager() {}
    
    /*
    *Attempts to log the user in by obtaining the username and password
    *combo from the database.
    @return null if user not found, user object if found
    */
    public static User attemptLogIn(ResourceBundle rb) {
        String userName = getLogInUserName(rb);
        String password = getLogInPassword(rb);
        
        try(Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.getConn().createStatement()) {
            
            ResultSet rs = stmt.executeQuery(String.format("select * from user where userName='%s' AND password='%s'", 
                userName, password));
            
            while(rs.next()) {
                if(rs.getString("userName").equals(userName)
                        && rs.getString("password").equals(password)) {
                    int userid = rs.getInt("userid");
                    String username = rs.getString("userName");
                    
                    DatabaseConnection.closeConnection();
                    return new User(userid, username, true); 
                }
            }   

            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println(rb.getString("error"));
        } 
        
        return new User(-1, userName, false);
    }
    
    
    public static String getLogInUserName(ResourceBundle rb) {
        System.out.print(rb.getString("username"));
        return sc.nextLine();

    }

    public static String getLogInPassword(ResourceBundle rb) {
        System.out.print(rb.getString("password"));
        return sc.nextLine();
    }
    
}
