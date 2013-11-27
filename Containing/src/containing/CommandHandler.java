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
    static HashMap<Long, Command> queuedCommands = new HashMap<>();
    static HashMap<Integer, Long> idTime = new HashMap<>();

    public static void addCommand(Command cmd){
        Date date = new Date();
        queuedCommands.put(date.getTime() / 1000, cmd);
    }
    
    public static Command handle(String input) {
        String prefix = input.split(":", 1)[0];
        switch (prefix) {
            case "PORT":
                return new Command("port", Settings.port);

            default:
                return null;
        }
    }
    
    public static List<Command> getNewCommands(int id){
        List<Command> commands = new ArrayList<>();
        Date date = new Date();
        long currentTime = date.getTime() / 1000;
        if(idTime.get(id) != null){
            long lastTime = idTime.get(id);
            for(Entry<Long, Command> entry : queuedCommands.entrySet()){
                if(entry.getKey() >= lastTime){
                    commands.add(entry.getValue());
                }
            }
        }
        else{
            idTime.put(id, currentTime);
        }
        return commands;
    }
}
