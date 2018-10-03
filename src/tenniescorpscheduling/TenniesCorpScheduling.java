/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tenniescorpscheduling;

/**
 *
 * @author Joshua
 */
public class TenniesCorpScheduling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            DBConnection.makeConnection();

            DBConnection.closeConnection();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
