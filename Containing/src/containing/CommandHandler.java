/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

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
        String[] command = input.split(":", 2);
        String prefix = command[0];
        if(command.length == 2){
            String arg = command[1];
        }

        switch (prefix) {
            case "PORT":
                return new Command("port", new Command("test", "test"));
                

            case "STATS":
                HashMap<String, Integer> stats = new HashMap<>();
                for(int i = 0; i < 7; i++){
                    Random rand = new Random();
                    int randomNumber = rand.nextInt(10);
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
    
    public static List<Command> getNewCommands(int id, boolean app){
        List<Command> commands = new ArrayList<>();
        
        if(idLastID.get(id) != null){
            int lastID = idLastID.get(id);
            for(Entry<Integer, Command> entry : queuedCommands.entrySet()){
                if(entry.getKey() > lastID && entry.getValue().getApp() == app){
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
