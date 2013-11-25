/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;
import containing.Road.Route;
import containing.Vector3f;

/**
 *
 * @author Miriam
 */
public class AGV extends InternVehicle {
    
    private static int counter;
    private int id;
    private Platform currentPlatform;
    private static int capicityAGV = 1;
    private static int maxSpeedLoaded = 20;
    private static int maxSpeedUnloaded = 40;
    
    public AGV(Platform currentPlatform, Vector3f startPosition){
        super(capicityAGV,startPosition );
        id = counter;
        counter++;
        this.currentPlatform = currentPlatform; //storageplatform @ default?
    }
    
    public void followRoute(Route route){ //call method platform when route is finished
    
    } 
    

}
