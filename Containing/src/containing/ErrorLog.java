package containing;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Robert
 */
public class ErrorLog {

    static final String LOGFILE = "ErrorLog.txt";

    /**
     * Writes an error message and an exception to the logfile
     * @param msg Error message
     * @param ex Thrown exception
     */
    public static void logMsg(String msg, Exception ex) {
        System.out.println("ERROR: " + msg +"(" +ex.getMessage() +")");
        try{
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg +"(" +ex.getMessage() +")\n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() +")");
        }
    }
    
    /**
     * Writes an error message to the logfile
     * @param msg Error message
     */
    public static void logMsg(String msg) {
        System.out.println("ERROR: " + msg);
        try{
            FileWriter writer = new FileWriter(LOGFILE, true);
            writer.append(msg +"\n");
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error while opening logfile(" + e.getMessage() +")");
        }
    }

}
