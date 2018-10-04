/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenniescorpscheduling;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Joshua Tennies
 */
public class DataRetriever {
    public static ResultSet getUserInfo(String userName, String password) throws SQLException {
        Statement stmt = DatabaseConnection.getConn().createStatement();
        stmt.executeQuery(String.format("SELECT * FROM user WHERE userName='%s' AND password='%s'", 
                userName, password));
        
        return null;
    }
}
