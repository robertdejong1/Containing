/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package containing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
public class CommandHandler {
    static List<Command> queuedCommands = new ArrayList<>();

    public static void addCommand(Command cmd){
        queuedCommands.add(cmd);
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
    
    public static List<Command> getNewCommands(){
        return queuedCommands;
    }
}
