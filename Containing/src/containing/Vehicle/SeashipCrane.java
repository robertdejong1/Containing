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
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public class SeashipCrane extends Crane {

    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    //hier SeashipCrane specific variables
    
    public SeashipCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.SEASHIPCRANE, width, length);

    }
    

 
    
    @Override
    public int getMaxSpeedUnloaded(){return 4;}
    @Override
    public int getMaxSpeedLoaded(){return 0;}
}
