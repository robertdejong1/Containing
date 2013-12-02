/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public Command(String command, Object object) {
        this(command, object, false);
    }

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

    @Override
    public String toString() {
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
