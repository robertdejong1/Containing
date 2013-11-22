/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing;

import org.json.simple.JSONObject;

/**
 *
 * @author Robert
 */
public class Command {
    private String command;
    private Object object;

    public Command(String command, Object object) {
        this.command = command;
        this.object = object;
    }

    public String getCommand() {
        return command;
    }

    public Object getObject() {
        return object;
    }
    
    @Override
    public String toString(){
        JSONObject json = new JSONObject();
        json.put(this.command, this.object);
        return json.toString();
    }
}
