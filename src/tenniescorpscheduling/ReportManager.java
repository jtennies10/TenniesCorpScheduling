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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

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
        for (int i = 0; i < 12; i++) {
            int newAppt = 0, returningAppt = 0, complaintAppt = 0, customAppt = 0;
            currentMonth = currentMonth.plusMonths(1);

            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE start BETWEEN '%s' AND '%s'", currentMonth,
                    currentMonth.with(TemporalAdjusters.lastDayOfMonth()).plusHours(23)));

            while (rs.next()) {
                String type = rs.getString("type");
                switch (type) {
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

    /*
    Generates a report of appointments grouped by consultant for the month
     */
    private void generateAppointmentsByConsultant(Statement stmt) throws SQLException {

        //collect all the users in the database
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");

        //populate an array list of users with the resultset
        ArrayList<User> list = new ArrayList();
        while (rs.next()) {
            list.add(new User(rs.getInt("userId"), rs.getString("userName")));
        }

        rs.close();

        System.out.println("Schedule for each consultant for the next month");

        //iterate through the list, printing each users schedule for the next month
        for (User u : list) {

            System.out.println(u);

            rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE (start BETWEEN '%s' AND '%s') AND userId=%d", LocalDate.now().toString(),
                    LocalDate.now().plusMonths(1).toString(), u.getUserId()));

            while (rs.next()) {
                System.out.println("\tID: " + rs.getInt("appointmentId") + " Customer ID: "
                        + rs.getInt("customerId") + " at " + rs.getTimestamp("start"));
            }
            rs.close();
        }
    }

    private void generateAppoinmentsByRatio(Statement stmt) throws SQLException {
        //get the first of the month, in the zeroth hour
        //subtract a month so that the current month is used
        //in the first iteration below
        LocalDateTime currentMonth = LocalDateTime.now().with(
                TemporalAdjusters.firstDayOfMonth()).minusHours(LocalDateTime.now().getHour())
                .minusMonths(1);

        //iterate through each month gathering the report data
        for (int i = 0; i < 12; i++) {
            int newAppt = 0, returningAppt = 0;
            currentMonth = currentMonth.plusMonths(1);

            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM appointment "
                    + "WHERE start BETWEEN '%s' AND '%s'", currentMonth,
                    currentMonth.with(TemporalAdjusters.lastDayOfMonth()).plusHours(23)));

            while (rs.next()) {
                String type = rs.getString("type");
                switch (type) {
                    case "New Customer":
                        newAppt++;
                        break;
                    case "Returning Customer":
                        returningAppt++;
                        break;
                    default: //do nothing
                        break;
                }
            }

            rs.close();

            int gcd = findGCD(newAppt, returningAppt);
            
            System.out.println(currentMonth.getMonth() + " " + currentMonth.getYear());
            if (gcd != 0) {
                newAppt /= gcd;
                returningAppt /= gcd;
                System.out.println("\tRatio of New:Returning - " + newAppt
                        + ":" + returningAppt);
            } else {
                System.out.println("\tZero found, therefore no ratio available");
            }
        }
    }

    //Find the GCD using the well known Euclid's algorithm
    private static int findGCD(int n1, int n2) {
        if(n1 == 0 || n2 == 0) {
            return 0;
        }
        
        if (n1 % n2 == 0) {
            return n2;
        } else {
            return findGCD(n2, n1 % n2);
        }

    }
}
