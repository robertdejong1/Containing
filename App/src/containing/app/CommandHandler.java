package containing.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Robert
 */
public class CommandHandler {

	volatile static List<String> queuedCommands = new ArrayList<String>();
	
    public static void handle(String input){
        JSONObject json = (JSONObject) JSONValue.parse(input);

        if(json.get("stats") != null){
        	
        	JSONObject stats = (JSONObject) json.get("stats");

        	HashMap<String, Double> map = new HashMap<String, Double>();
        	map.put("train", Double.parseDouble(stats.get("train").toString()));
        	map.put("truck", Double.parseDouble(stats.get("truck").toString()));
        	map.put("seaShip", Double.parseDouble(stats.get("seaShip").toString()));
        	map.put("barge", Double.parseDouble(stats.get("barge").toString()));
        	map.put("storage", Double.parseDouble(stats.get("storage").toString()));
        	map.put("agv", Double.parseDouble(stats.get("agv").toString()));
        	map.put("other", Double.parseDouble(stats.get("other").toString()));
        	
        	if(MainActivity.getStats() == null){
        		MainActivity.showGraphView(map);
        	}
        	else{
        		MainActivity.updateStats(map);
        	}
        }
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
