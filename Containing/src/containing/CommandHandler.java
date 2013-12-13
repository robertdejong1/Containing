package containing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Robert
 */
public class CommandHandler {
    static volatile ConcurrentHashMap<Integer, Command> queuedCommands = new ConcurrentHashMap<>();
    static volatile ConcurrentHashMap<Integer, Integer> idLastID = new ConcurrentHashMap<>();
    static volatile int counter = 0;

    /**
     * Adds a command to the command queue
     * @param cmd The command to be added
     */
    public static void addCommand(Command cmd){
        queuedCommands.put(getNewId(), cmd);
    }
    
    /**
     * Handles a given input string
     * @param input The string to be handled
     * @return A reply Command
     */
    public static Command handle(String input) {
        String[] command = input.split(":", 2);
        String prefix = command[0];
        if(command.length == 2){
            String arg = command[1];
        }

        switch (prefix) {
            case "PORT":
                return new Command("port", new Command("port", Settings.port));
                

            case "STATS":
                HashMap<String, Double> stats = new HashMap<>();
                for(int i = 0; i < 7; i++){
                    Random rand = new Random();
                    Double randomNumber = rand.nextDouble();
                    switch(i){
                        case 0: stats.put("train", randomNumber); break;
                        case 1: stats.put("truck", randomNumber); break;
                        case 2: stats.put("seaShip", randomNumber); break;
                        case 3: stats.put("barge", randomNumber); break;
                        case 4: stats.put("storage", randomNumber); break;
                        case 5: stats.put("agv", randomNumber); break;
                        case 6: stats.put("other", randomNumber); break;
                    }
                }
                
                return new Command("stats", stats);
                
            default:
                return null;
        }
    }
    
    /**
     * Checks wether there are new commands available for given id and app
     * @param id The id of the client connection
     * @param app Boolean to only check for commands to the app
     * @return List of new commands
     */
    public static List<Command> getNewCommands(int id, boolean app){
        List<Command> commands = new ArrayList<>();
        
        if(idLastID.get(id) != null){
            int lastID = idLastID.get(id);
            
            Iterator it = queuedCommands.entrySet().iterator();
            while(it.hasNext()){
                Entry<Integer, Command> entry = (Entry) it.next();
                if(entry.getKey() > lastID && entry.getValue().getApp() == app){
                    commands.add(entry.getValue());
                }
            }
        }
        idLastID.put(id, getLastID());
        
        return commands;
    }
    
    /**
     * Adds the last Command id to a client connection id
     * @param id The client connection id
     */
    public static void addidTime(int id){
        idLastID.put(id, getLastID());
    }
    
    public static int getNewId(){
        return ++counter;
    }
    
    public static int getLastID(){
        int id = 0;
        for(Entry<Integer, Command> entry : queuedCommands.entrySet()){
            if(entry.getKey() > id){
                id = entry.getKey();
            }
        }
        return id;
    }
}
