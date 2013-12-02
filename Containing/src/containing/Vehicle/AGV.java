/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Road.Route;
import containing.Vector3f;

/**
 *
 * @author Miriam
 */
public class AGV extends InternVehicle {
    private int timeCounter = 0;
    private static int counter;
    private int id;
    public static float width = 2.5f; //????????
    public static float length = 3.5f; //??????????
    private static int capicityAGV = 1;

    public AGV(Platform currentPlatform, Vector3f startPosition)
    { 
        super(capicityAGV,startPosition, currentPlatform, Type.AGV);
        id = counter;
        counter++;
        this.currentPlatform = currentPlatform; //storageplatform @ default?
    }
    

    
    @Override
    public int getMaxSpeedUnloaded(){return 40;}
    @Override
    public int getMaxSpeedLoaded(){return 20;}
    

    
    public void setStatus(Status status){
        this.status = status;
    }
    
    public void update(){
       super.update();
       if (this.status == Status.MOVING){this.route.follow(this);}
    }
    
    
    

}
