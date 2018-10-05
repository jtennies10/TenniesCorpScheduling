/*
 * Class used to retrieve data from the database with help from the 
 * DatabaseConnection class.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataRetriever {
    
    //private constructor so the class is not instantiated
    private DataRetriever() {}
    
    public static User attemptLogIn(String userName, String password) {
        
        try {
            DatabaseConnection.makeConnection();
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error connecting to the database");
            return null;
        }
        
        try(Statement stmt = DatabaseConnection.getConn().createStatement()) {
            
            ResultSet rs = getUserInfo(stmt, userName, password);
            
            while(rs.next()) {
                if(rs.getString("userName").equals(userName)
                        && rs.getString("password").equals(password)) {
                    return new User(rs.getInt("userid"), rs.getString("userName")); 
                }
            }   

            DatabaseConnection.closeConnection();
            
        } catch(ClassNotFoundException | SQLException e) {
            System.out.println("There was a problem communicating with the database.");
            System.out.println(e.getMessage());
            return null;
        } 
        
        return null;
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
        System.out.printf("SELECT * FROM user WHERE userName='%s' AND password='%s'", 
                userName, password);
        
        return rs;
    }
    
    
}
