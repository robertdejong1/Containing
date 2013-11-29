/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author Robert
 */
public class CommandHandler {
    static volatile HashMap<Integer, Command> queuedCommands = new HashMap<>();
    static volatile HashMap<Integer, Integer> idLastID = new HashMap<>();
    static volatile int counter = 0;

    public static void addCommand(Command cmd){
        queuedCommands.put(getNewId(), cmd);
    }
    
    public static Command handle(String input) {
        String prefix = input.split(":", 1)[0];
        switch (prefix) {
            case "PORT":
                return new Command("port", Settings.port);

            case "STATS":
                HashMap<String, Integer> stats = new HashMap<>();
                stats.put("train", 1);
                stats.put("truck", 1);
                stats.put("seaShip", 1);
                stats.put("barge", 1);
                stats.put("opslag", 1);
                stats.put("agv", 1);
                stats.put("other", 1);
                return new Command("stats", stats);
                
            default:
                return null;
        }
    }
    
    public static List<Command> getNewCommands(int id){
        List<Command> commands = new ArrayList<>();

        if(idLastID.get(id) != null){
            int lastID = idLastID.get(id);
            for(Entry<Integer, Command> entry : queuedCommands.entrySet()){
                if(entry.getKey() > lastID){
                    commands.add(entry.getValue());
                }
            }
        }
        idLastID.put(id, getLastID());
        
        return commands;
    }
    
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
