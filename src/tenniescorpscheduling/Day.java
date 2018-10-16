/*
 * Defines the Day class which holds the appointments for the given day 
 * 
 */
package tenniescorpscheduling;

import java.time.DayOfWeek;
import java.util.ArrayList;


public class Day {
    final private int dayOfMonth;
    final private DayOfWeek dayOfWeek;
    private ArrayList<Appointment> appointments;
    
    public Day(int dayOfMonth, DayOfWeek dayOfWeek) {
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        appointments = new ArrayList();
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
    
    public void addAppointment(Appointment appt) {
        appointments.add(appt);
    }
    
    /*
    Overrides toString method returning a formatted string composed of 
    string formatted with the formatAppointmentBlock method
    @return the formatted string representing the Day object
    */
    @Override
    public String toString() {
        return String.format("%-15s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s|%-24s\n",
                (dayOfMonth + " " + dayOfWeek), formatAppointmentBlock(8), formatAppointmentBlock(9),
                formatAppointmentBlock(10), formatAppointmentBlock(11),
                formatAppointmentBlock(12), formatAppointmentBlock(13),
                formatAppointmentBlock(14), formatAppointmentBlock(15),
                formatAppointmentBlock(16));
    }
    
    /*
    Formats the passed in hour with the possible appointments that start within that
    hour, which are constrainted to only starting at increments of 15 minutes
    @return the formatted hour
    */
    private String formatAppointmentBlock(int hour) {
        String[] apptsInHour = {"","","",""};
        for(Appointment a : appointments) {
            if(a.getStart().getHour() == hour) {
                apptsInHour[a.getStart().getMinute() / 15] = 
                        (String.valueOf(a.getAppointmentId()));
            }
        }
        
        return String.format("%-6s%-6s%-6s%-6s", apptsInHour[0], apptsInHour[1],
                apptsInHour[2], apptsInHour[3]);
    }
    
    
    
}
