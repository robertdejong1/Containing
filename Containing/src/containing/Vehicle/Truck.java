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
public class Truck extends ExternVehicle{
    private static int counter;
    private int truckID;
    static int capicity = 1;
    protected int timeCounter = 0;
    public static float width = 3f; //????????
    public static float length = 5f; //??????????
    private static int nrContainersDepth= 1;
    private static int nrContainersHeight = 1;
    private static int nrContainersWidth = 1;
    
    public Truck(Date arrivalDate, float arrivalTime, Platform platform, String company)
    { 
        
        super(arrivalDate, arrivalTime, nrContainersDepth,nrContainersHeight,nrContainersWidth, platform, company, Type.TRUCK); //true if vehicle comes to load, otherwise false
        
        truckID = counter;
        
        counter++;
    }
   
  
    @Override
    public int getMaxSpeedUnloaded(){return 30;}
    @Override
    public int getMaxSpeedLoaded(){return 15;}
    
     public void update(){
        timeCounter++;
    }
}
