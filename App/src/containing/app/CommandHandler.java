package containing.app;

import android.util.Base64;
import containing.Command;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

    static void handle(String input) {
        //Handle de command
        Command cmd = (Command) decode(input);
        if(cmd == null){
        	return;
        }
        
        if(cmd.getCommand().equals("stats")){
        	if(MainActivity.getStats() == null){
        		MainActivity.showGraphView((HashMap<String, Double>) cmd.getObject());
        	}
        	else{
        		MainActivity.updateStats((HashMap<String, Double>) cmd.getObject());
        	}
        }
    }

    private static Object decode(String encoded) {
        byte[] bytes;
        bytes = Base64.decode(encoded, Base64.DEFAULT);

        try {
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            return si.readObject();
        } catch (Exception e) {
            System.out.println("An error occured while decoding base64");
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
}