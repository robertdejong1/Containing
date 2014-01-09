package containing;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import containing.ClientHandler.ClientType;
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
    private ClientType clientType;

    /**
     * Creates a Command instance
     *
     * @param command The name of this command
     * @param object The data wich belongs to this command
     */
    public Command(String command, Object object) {
        this(command, object, ClientType.SIM);
    }

    /**
     * Creates a Command instance
     *
     * @param command The name of this command
     * @param object The data wich belongs to this command
     * @param type Client for this command
     */
    public Command(String command, Object object, ClientType type) {
        this.command = command;
        this.object = object;
        this.clientType = type;
    }

    public String getCommand() {
        return command;
    }

    public Object getObject() {
        return object;
    }

    public ClientType getClientType() {
        return this.clientType;
    }

    /**
     * Serializes this object and returns the value base64 encoded
     *
     * @return The base64 encoded serialized string of this Command
     */
    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean encode) {
        if (encode) {
            System.out.println(this.command);
            try {
                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream so = new ObjectOutputStream(bo);
                so.writeObject(this);
                so.flush();
                byte[] bytes = bo.toByteArray();
                return (Base64.encode(bytes));
            } catch (IOException e) {
                ErrorLog.logMsg("Error while serializing command object", e);
            }

            return null;
        }
        else{
            return this.getCommand().toString();
        }
    }
}
