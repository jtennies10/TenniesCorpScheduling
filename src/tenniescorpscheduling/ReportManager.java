/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenniescorpscheduling;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 *
 * @author Joshua
 */
class ReportManager {

    public void printCustomerOptions() {
        System.out.println("1. Number of appointment types by month");
        System.out.println("2. Schedule for each consultant");
        System.out.println("3. Ratio of new customer to returning customer "
                + "appointments by month");
        System.out.println("4. Return to general options");
    }
    
    public void executeReportChoice(int userChoice) {
         try (Connection conn = DatabaseConnection.makeConnection();
                Statement stmt = DatabaseConnection.conn.createStatement()) {

            switch (userChoice) {
                case 1:
                    //number of appointment types by month
                    generateAppointmentsByType(stmt);
                    
                    break;
                case 2:
                    //schedule for each consultant
                    generateAppointmentsByConsultant(stmt);
                    
                    break;
                case 3:
                    //ratio of new customers to returning by month
                    generateAppoinmentsByRatio(stmt); 
                    
                    break;
                default: //case 4 and default
                    System.out.println("Returning to general options");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error communicating with the database.");
            System.out.println(e.getMessage());
        }
    }


    private void generateAppointmentsByType(Statement stmt) throws SQLException {
        //get the first of the month, in the zeroth hour
        //subtract a month so that the current month is used
        //in the first iteration below
        LocalDateTime currentMonth = LocalDateTime.now().with(
                TemporalAdjusters.firstDayOfMonth()).minusHours(LocalDateTime.now().getHour())
                .minusMonths(1);
        
        //iterate through each month gathering the report data
        for(int i = 0; i < 12; i ++) {
            int newAppt = 0, returningAppt = 0, complaintAppt = 0, customAppt = 0;
            currentMonth = currentMonth.plusMonths(1);
            
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE start BETWEEN '%s' AND '%s'", currentMonth, 
                    currentMonth.with(TemporalAdjusters.lastDayOfMonth()).plusHours(24)));
            
            while(rs.next()) {
                String type = rs.getString("type");
                switch(type) {
                    case "New Customer":
                        newAppt++;
                        break;
                    case "Returning Customer":
                        returningAppt++;
                        break;
                    case "Customer Complaint":
                        complaintAppt++;
                        break;
                    default:
                        customAppt++;
                }
            }
            
            rs.close();
            
            //print out the data for the month
            System.out.println(currentMonth.getMonth() + " " + currentMonth.getYear());
            System.out.println("\tNew Customer Appointments: " + newAppt);
            System.out.println("\tReturning Customer Appointments: " + returningAppt);
            System.out.println("\tCustomer Complaint Appointments: " + complaintAppt);
            System.out.println("\tCustom Appointments: " + customAppt);
        }
    }

    private void generateAppointmentsByConsultant(Statement stmt) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void generateAppoinmentsByRatio(Statement stmt) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
