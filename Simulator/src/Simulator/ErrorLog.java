/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Robert
 */
public class ErrorLog {

    static final String LOGFILE = "ErrorLog.txt";

    public static void logMsg(String msg, Exception ex) {
        System.out.println("ERROR: " + msg + "(" + ex.getMessage() + ")");
        try {
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg +"(" +ex.getMessage() +")\n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        }
    }

    public static void logMsg(String msg) {
        System.out.println("ERROR: " + msg);
        try {
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg +"\n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        }
    }
}
