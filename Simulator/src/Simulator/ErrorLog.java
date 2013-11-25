/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.io.FileNotFoundException;
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
            PrintWriter writer = new PrintWriter(LOGFILE, "UTF-8");
            writer.append(msg +"(" +ex.getMessage() +")");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        }
    }

    public static void logMsg(String msg) {
        System.out.println("ERROR: " + msg);
        try {
            PrintWriter writer = new PrintWriter(LOGFILE, "UTF-8");
            writer.println(msg);
        } catch (FileNotFoundException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() + ")");
        }
    }
}
