/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Robert
 */
public class ErrorLog {

    static final String LOGFILE = "ErrorLog.txt";

    public static void logMsg(String msg, Exception ex) {
        System.out.println("ERROR: " + msg +"(" +ex.getMessage() +")");
        try{
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg +"(" +ex.getMessage() +")");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() +")");
        }
    }
    
        public static void logMsg(String msg) {
        System.out.println("ERROR: " + msg);
        try{
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg);
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() +")");
        }
    }

}
