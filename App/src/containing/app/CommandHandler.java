package containing.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Robert
 */
public class CommandHandler {

	volatile static List<String> queuedCommands = new ArrayList<String>();
	
    public static Boolean handle(String json){
        JSONParser parser = new JSONParser();
    	
    	
        JSONObject result;
		try {
			result = (JSONObject) parser.parse(json);
			
			System.out.println(result.get("stats").toString());
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
