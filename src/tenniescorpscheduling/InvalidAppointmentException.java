/*
 * Exception class that is thrown when an appointment is attempted to be made
 * that is either outside business hours in local time or overlaps with another
 * appointment the user has. Also thrown when an appointment has a start time
 * that does not meet the set time or an invalid length.
 */
package tenniescorpscheduling;

public class InvalidAppointmentException extends RuntimeException {
    public InvalidAppointmentException(String message) {
        super(message);
    }
}
