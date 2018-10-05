/*
 * Defines the User class which is instantiated upon successful log in.
 * Only takes the necessary fields from the database needed for the program.
 */
package tenniescorpscheduling;

/**
 *
 * @author Joshua
 */
class User {
    private int userid;
    private String userName;

    public User(int userid, String userName) {
        this.userid = userid;
        this.userName = userName;
    }

    public int getUserid() {
        return userid;
    }

    public String getUserName() {
        return userName;
    }

    
    
    
}
