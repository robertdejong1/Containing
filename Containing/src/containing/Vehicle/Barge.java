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
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Barge extends ExternVehicle {

    public static float width = 5f; //????????
    public static float length = 10f; //??????????
    public static int nrContainersDepth = 6;
    public static int nrContainersHeight = 6;
    public static int nrContainersWidth = 10;

    
    public Barge(Date arrivalDate, float arrivalTime, Platform platform, String company)
    {
        
        super(arrivalDate, arrivalTime, nrContainersDepth,nrContainersWidth,nrContainersHeight, platform, company, Type.BARGE); //true if vehicle comes to load, otherwise false
       
    
    }
    

    
    @Override
    public int getMaxSpeedUnloaded(){return 25;}
    @Override
    public int getMaxSpeedLoaded(){return 30;}
}
