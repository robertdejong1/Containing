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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Robert
 */

enum Type {TRUCK, AGV, BARGE, BARGECRANE, SEASHIP, SEASHIPCRANE, TRAIN, TRAINCRANE, TRUCKCRANE, STORAGECRANE }

public class CommandHandler {

    private static volatile List<String> queuedCommands = new ArrayList<String>();

    static void handle(String input) {
        //Handle de command
        Command cmd = (Command) decode(input);
        
        if (cmd.getCommand().equals("enterExternVehicle"))
        {
            HashMap<String, Object> map = (HashMap<String, Object>)cmd.getObject();
            int id = Integer.parseInt(map.get("id").toString());
            System.out.println(""+id);
                        
            Type type = Type.valueOf(map.get("vehicleType").toString());
            System.out.println("" + type.toString());
            Object[][][] containers = (Object[][][]) map.get("cargo");
                    
            switch (type)
            {
                case TRAIN:
                    for (int i = 0; i < containers.length; i++)
                    {
                        if (containers[i][0][0] != null)
                        {
                            
                        }
                    }
                    break;
                    
                default:
                    break;
            }
        }
        
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
}
