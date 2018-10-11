/*
 * Defines the Appointment class which models the appointment table in the database
 */
package tenniescorpscheduling;

import java.time.ZonedDateTime;

/**
 *
 * @author Joshua
 */
public class Appointment {
   final private int appointmentId;
   private int customerId;
   private int userId;
   private String title;
   private String description;
   private String location;
   private String contact;
   private String type;
   private String url;
   private ZonedDateTime start; //time is stored in utc
   private int lengthInMinutes;

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, String contact, String type, String url, ZonedDateTime start, int lengthInMinutes) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.lengthInMinutes = lengthInMinutes;
    }

    public int getAppointmentId() {
        return appointmentId;
    }
    
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public void setLengthInMinutes(int lengthInMinutes) {
        this.lengthInMinutes = lengthInMinutes;
    }
    
    
   
    
   

   
   
   
}
