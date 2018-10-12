/*
 * Defines the Day class which holds the appointments for the given day 
 * 
 */
package tenniescorpscheduling;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author Joshua
 */
public class Day {
    final private int dayOfMonth;
    final private DayOfWeek dayOfWeek;
    private ArrayList<Appointment> appointments;
    
    public Day(int dayOfMonth, DayOfWeek dayOfWeek ) {
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
    
    //TODO:override toString method
    
    
}
