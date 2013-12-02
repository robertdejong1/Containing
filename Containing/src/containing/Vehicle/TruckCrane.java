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
public class TruckCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    public static float width = 3f; //????????
    public static float length = 5f; //??????????
    
    //hier TruckCrane specific variables
   
    public TruckCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.TRUCKCRANE, width, length);
        id = counter;
        this.setID(id);
        counter++;
    }

    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 15;}

}
