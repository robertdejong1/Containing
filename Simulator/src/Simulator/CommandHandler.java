/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Robert
 */
public class CommandHandler {

    private static volatile List<String> queuedCommands = new ArrayList<String>();
    
    static void handle(String input){
        
        JSONObject obj = (JSONObject) JSONValue.parse(input);
        System.out.println(obj);
        
        //Handle de command
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
