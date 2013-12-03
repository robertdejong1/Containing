/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import containing.Command;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert
 */
enum Type {

    TRUCK, AGV, BARGE, BARGECRANE, SEASHIP, SEASHIPCRANE, TRAIN, TRAINCRANE, TRUCKCRANE, STORAGECRANE
}

public class CommandHandler {

    private static volatile List<String> queuedCommands = new ArrayList<String>();
    private static volatile List<Command> stackedCommands = new ArrayList<Command>();

    static void handle(String input) {
        //Handle de command
        Command cmd = (Command) decode(input);
        stackedCommands.add(cmd);
    }

    private static Object decode(String encoded) {
        byte[] bytes;
        try {
            bytes = Base64.decode(encoded);
        } catch (Base64DecodingException e) {
            ErrorLog.logMsg("Error while decoding base64", e);
            return null;
        }

        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            ErrorLog.logMsg("Error while decoding serialization", e);
        }
        return null;
    }

    public static List<String> getCommands() {
        return queuedCommands;
    }

    public static void clearCommands() {
        queuedCommands.clear();
    }

    public static void addCommand(String cmd) {
        queuedCommands.add(cmd);
    }

    public static boolean newCommandsAvailable() {
        return queuedCommands.size() > 0;
    }

    public static Command getStackedCommand() {
        if (stackedCommands.size() > 0) {
            Command cmd = stackedCommands.get(0);
            stackedCommands.remove(0);
            return cmd;
        } else {
            return null;
        }
    }
}
