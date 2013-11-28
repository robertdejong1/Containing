package containing.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Robert
 */
public class CommandHandler {

	public volatile static List<String> queuedCommands = new ArrayList<String>();
	
    public static Boolean handle(String json){
        JSONParser parser = new JSONParser();
        Object obj;
        
        try{
            obj = parser.parse(json);
            System.out.println(obj.toString());
           
        }
        catch(ParseException e){
            return false;
        }
        //Handle de commands
        
        MainActivity.showDialog(obj.toString());
        
        return true;
    }
    
    public static List<String> getCommands(){
        List<String> cmds = queuedCommands;
        queuedCommands.clear();
        return cmds;
    }
    
    public static void addCommand(String cmd){
    	queuedCommands.add(cmd);
    	System.out.println("Added: " +cmd);
    }
}
