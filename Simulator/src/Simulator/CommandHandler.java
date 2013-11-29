/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Robert
 */
public class CommandHandler {

    private static volatile List<String> queuedCommands = new ArrayList<String>();
    
    static Boolean handle(String json){
        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(json);
            System.out.println(obj.toString());
        }
        catch(ParseException e){
            ErrorLog.logMsg("Error while parsing json", e);
            return false;
        }
        //Handle de commands
        
        
        return true;
    }
    
    public static List<String> getCommands(){
        return queuedCommands;
    }
    
    public static void clearCommands(){
    	queuedCommands.clear();
    }
    
    public static void addCommand(String cmd){
    	queuedCommands.add(cmd);
    }
    
    public static boolean newCommandsAvailable(){
    	return queuedCommands.size() > 0;
    }
}
