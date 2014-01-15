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
import containing.Settings;
import containing.Vector3f;
import static containing.Vehicle.Barge.nrContainersDepth;
import static containing.Vehicle.Barge.nrContainersHeight;
import static containing.Vehicle.Barge.nrContainersWidth;
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Truck extends ExternVehicle{
    

    public static float width = 3f; //????????
    public static float length = 5f; //??????????
    public static int nrContainersDepth= 1;
    public static int nrContainersHeight = 1;
    public static int nrContainersWidth = 1;
    
    public Truck(Date arrivalDate, float arrivalTime, Platform platform, String company)
    { 
        
        super(arrivalDate, arrivalTime, nrContainersDepth,nrContainersWidth,nrContainersHeight, platform, company, Type.TRUCK); //true if vehicle comes to load, otherwise false
        this.maxSpeedLoaded = 50;
        this.maxSpeedUnloaded = 60;
        this.position = new Vector3f(815f * Settings.METER,5.5f,600f*Settings.METER); 

    }
   
  
    @Override
    public int getMaxSpeedUnloaded(){return 30;}
    @Override
    public int getMaxSpeedLoaded(){return 15;}
    
  
}
