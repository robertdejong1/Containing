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
public class StorageCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    //hier StorageCrane specific variables
   
    public StorageCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.STORAGECRANE, width, length);
        id = counter;
        this.setID(id);
        counter++;
    }
    


    
    public void reset(){super.reset();}
    
    @Override
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}
    
}
