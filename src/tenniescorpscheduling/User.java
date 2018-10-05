/*
 * Defines the User class which is instantiated upon successful log in.
 * Only takes the necessary fields from the database needed for the program.
 */
package tenniescorpscheduling;

import java.util.Scanner;

/**
 *
 * @author Joshua
 */
class User {
    private int userid;
    private String userName;
    private Scanner sc = new Scanner(System.in);

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
    
    public int getUserChoice() {
        System.out.print("Choice: ");
        return sc.nextInt();
    }

    
    
    
}
