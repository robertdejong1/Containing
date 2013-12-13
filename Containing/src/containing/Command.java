package containing;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author Robert
 */
public class Command implements Serializable {

    private String command;
    private Object object;
    private boolean app;

    /**
     * Creates a Command instance
     * @param command The name of this command
     * @param object The data wich belongs to this command
     */
    public Command(String command, Object object) {
        this(command, object, false);
    }

     /**
     * Creates a Command instance
     * @param command The name of this command
     * @param object The data wich belongs to this command
     * @param app True if this command is only meant for the app, false for the simulator
     */
    public Command(String command, Object object, boolean app) {
        this.command = command;
        this.object = object;
        this.app = app;
    }

    public String getCommand() {
        return command;
    }

    public Object getObject() {
        return object;
    }

    public boolean getApp() {
        return this.app;
    }

    /**
     * Serializes this object and returns the value base64 encoded
     * @return The base64 encoded serialized string of this Command
     */
    @Override
    public String toString() {
        System.out.println(this.command);
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            byte[] bytes = bo.toByteArray();
            return(Base64.encode(bytes));
        } catch (IOException e) {
           ErrorLog.logMsg("Error while serializing command object", e);
        }
        
        return null;
    }
}
