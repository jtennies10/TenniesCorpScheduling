/*
 * Defines the User class, only takes the necessary fields from the 
 * database needed for the program.
 */
package tenniescorpscheduling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Scanner;


class User {

    private Scanner sc = new Scanner(System.in);
    final private File FILE_NAME = new File("userActivity.txt");

    final private boolean loginSuccessful;
    private int userid;
    private String userName;
    
    public User(int userId, String userName) {
        this(userId, userName, false);
    }

    public User(int userid, String userName, boolean logInSuccessful) {
        this.userid = userid;
        this.userName = userName;
        this.loginSuccessful = logInSuccessful;
    }

    public int getUserId() {
        return userid;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserChoice() {
        System.out.print("Choice: ");
        return Integer.parseInt(sc.nextLine());
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    /*
    Prints the log in attempt to the file with the User information
    appending that the login was failed if the loginSuccessful member is 
    set to false
    */
    public void printLogInAttemptToFile() {
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));

            if (!FILE_NAME.exists()) {
                FILE_NAME.createNewFile();
            }
            
            bw.append(String.format("DateTime:%s Id:%d UserName:%s",
                        ZonedDateTime.now().toString(), userid, userName));
            
            if(!loginSuccessful) {
                bw.append(" - Failed");
            }
            
            bw.newLine();
            
            bw.close();

        } catch (IOException e) {
            System.out.println("There was an error writing the login attempt to "
                    + "the user activity file.");
        }
    }
    
    @Override
    public String toString() {
        return userid + " " + userName;
    }

}
