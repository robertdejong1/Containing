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
    
    static List<String> getCommands(){
        //Return een list met commando's naar de server
        List<String> commands = new ArrayList<String>();
        return commands;
    }
}
