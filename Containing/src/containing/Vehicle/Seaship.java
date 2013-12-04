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
public class Seaship extends ExternVehicle {

    public static float width = 10f; //????????
    public static float length = 20f; //??????????
    private static int nrContainersDepth= 90;
    private static int nrContainersHeight = 40;
    private static int nrContainersWidth = 40;
    
    public Seaship(Date arrivalDate, float arrivalTime, Platform platform, String company)
    {
        
        super(arrivalDate, arrivalTime,nrContainersDepth,nrContainersWidth,nrContainersHeight, platform, company, Type.SEASHIP); //true if vehicle comes to load, otherwise false
    }
   

    @Override
    public int getMaxSpeedUnloaded(){return 15;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
    
  
 
}
