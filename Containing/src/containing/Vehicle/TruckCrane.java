/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Robert
 */
public class TruckCrane extends Crane {

    public static float width = 3f; //????????
    public static float length = 5f; //??????????
    
    //hier TruckCrane specific variables
   
    public TruckCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.TRUCKCRANE, width, length);
    
    }
    
    @Override
    public void move(int direction)
    {
        List<Vector3f> route = new ArrayList<>();
        route.add(this.position);
        if (direction == -1)
        {
            route.add(new Vector3f(this.position.x - Container.width, this.position.y, this.position.z));
        }
        
        else 
        {
            route.add(new Vector3f(this.position.x + Container.width, this.position.y, this.position.z));
        }
        
        this.currentSpeed = (this.isLoaded) ? this.maxSpeedLoaded : this.maxSpeedUnloaded;
        
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getID());
        map.put("vehicleType", this.getVehicleType());
        map.put("motionPath", route); 
        map.put("speed", currentSpeed);
            
        CommandHandler.addCommand(new Command("moveCrane", map));
        
        
    }

    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 15;}

}
