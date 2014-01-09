package containing;

import containing.ClientHandler.ClientType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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
    public synchronized static void addCommand(Command cmd){
        queuedCommands.put(getNewId(), cmd);
        Settings.messageLog.AddMessage(cmd.getCommand());
    }
    
    /**
     * Handles a given input string
     * @param input The string to be handled
     * @return A reply Command
     */
    public synchronized static Command handle(String input) {
        String[] command = input.split(":", 2);
        String prefix = command[0];
        if(command.length == 2){
            String arg = command[1];
        }

        switch (prefix) {
            case "PORT":
                return new Command("port", new Command("port", Settings.port));
                

            case "STATS":
                HashMap<String, Double> stats = Settings.port.getStats();
                return new Command("stats", stats);
                
            default:
                return null;
        }
    }
    
    /**
     * Checks wether there are new commands available for given id and app
     * @param id The id of the client connection
     * @param type Client type that the commands are ment for
     * @return List of new commands
     */
    public synchronized static List<Command> getNewCommands(int id, ClientType type){
        List<Command> commands = new ArrayList<>();
        
        if(idLastID.get(id) != null){
            int lastID = idLastID.get(id);
            
            Iterator it = queuedCommands.entrySet().iterator();
            while(it.hasNext()){
                Entry<Integer, Command> entry = (Entry) it.next();
                if(entry.getKey() > lastID && entry.getValue().getClientType() == type){
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
    public synchronized static void addidTime(int id){
        idLastID.put(id, getLastID());
    }
    
    public synchronized static int getNewId(){
        return ++counter;
    }
    
    public synchronized static int getLastID(){
        int id = 0;
        for(Entry<Integer, Command> entry : queuedCommands.entrySet()){
            if(entry.getKey() > id){
                id = entry.getKey();
            }
        }
        return id;
    }
}
