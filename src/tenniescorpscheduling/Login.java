/*
 * Class used to interact with data from the database with help from the 
 * DatabaseConnection class.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Login {
    
    static Scanner sc = new Scanner(System.in);
    //private constructor so the class is not instantiated
    private Login() {}
    
    public static User attemptLogIn(ResourceBundle rb) {
        String userName = getLogInUserName(rb);
        String password = getLogInPassword(rb);
        
        try {
            DatabaseConnection.makeConnection();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println(rb.getString("error"));
            return null;
        }
        
        try(Statement stmt = DatabaseConnection.getConn().createStatement()) {
            
            ResultSet rs = getUserInfo(stmt, userName, password);
            
            while(rs.next()) {
                if(rs.getString("userName").equals(userName)
                        && rs.getString("password").equals(password)) {
                    int userid = rs.getInt("userid");
                    String username = rs.getString("userName");
                    
                    DatabaseConnection.closeConnection();
                    return new User(userid, username); 
                }
            }   

            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println(rb.getString("error"));
        } 
        
        return null;
    }
    
    
    public static String getLogInUserName(ResourceBundle rb) {
        System.out.print(rb.getString("username"));
        return sc.nextLine();

    }

    public static String getLogInPassword(ResourceBundle rb) {
        System.out.print(rb.getString("password"));
        return sc.nextLine();
    }
    
    /*
    Gets the user info entered based on the passed in arguments.
    @param userName the username of the user attempting to log in
    @param password the password of the user attempting to log in
    @return the ResultSet found by the query
    */
    private static ResultSet getUserInfo(Statement stmt, String userName, String password) throws SQLException, ClassNotFoundException {

        
        ResultSet rs = stmt.executeQuery(String.format("select * from user where userName='%s' AND password='%s'", 
                userName, password));
        
        return rs;
    }
    
    
}
