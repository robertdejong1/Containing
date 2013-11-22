/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;

/**
 *
 * @author Robert
 */
public class AGV extends InternVehicle {
    
    private static int counter;
    private int AGVid;
    private Platform currentPlatform;
    private static int capicityAGV = 1;
    private static int maxSpeedLoaded = 20;
    private static int maxSpeedUnloaded = 40;
    
    public AGV(Platform currentPlatform){
        super(capicityAGV, null);
        AGVid = counter;
        counter++;
        this.currentPlatform = currentPlatform; //storageplatform?
    }
    
    public void followRoute(){} //update platform agv currentplatform of?
    
    public Container unload(){return super.unload();}

    public void load(Container container){
        super.load(container);
    }
}
