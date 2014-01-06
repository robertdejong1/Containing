/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.VehicleOverflowException;
import containing.ParkingSpot.ParkingSpot;
import containing.Platform.Platform;
import containing.Road.Road;
import containing.Road.Route;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Robert
 */
public class StorageCrane extends Crane {

    public static float width = 5f; //????????
    public static float length = 6f; //??????????
    //hier StorageCrane specific variables
   
    public StorageCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.STORAGECRANE, width, length);

    }
    
   
    public void load(AGV agv, Vector3f containerStoragePosition, int i) throws VehicleOverflowException, CargoOutOfBoundsException, Exception{ //container from extern verhicle
    try
        {
            List<Vector3f> path = new ArrayList<Vector3f>();
            
            super.load(agv.unload());
            this.getCargo().get(0).setArrivalPosition(agv.getPosition());
            
            this.loadTime = 20*10;  
            this.unloadTime = 12*10;
            
            path.add(this.position);
            path.add(containerStoragePosition);
            path.add(this.position);
       
            HashMap<String, Object> map = new HashMap<>();
            
             map.put("craneid", this.getID());
             map.put("vehicleType", this.getVehicleType());
             map.put("clientid", agv.getID());
             map.put("duration", this.loadTime);
             map.put("container", cargo.get(0)); 
             map.put("indexnr", i);
             map.put("path", path);

             CommandHandler.addCommand(new Command("pickAndDropStorageCrane", map));
           
        }
        catch(Exception e)
        {
            throw e;
        }

        //this.timeCountDown = (int) liftTimeMax;
        //update: while (this.timeCounter < starttime + liftTimeMax + moveContainerSpeed * 2){} //aan het laden
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
     @Override
    public void update()
    {
       super.update();
       if (this.status == Status.LOADING )
       {
           this.loadTime--;
           if (this.loadTime <= 0)
           { 
               this.status = Status.UNLOADING;
        
               
               //command simulator
           }
           
       }
       
       if (this.status == Status.UNLOADING)
       {
           this.unloadTime--;
           if (this.unloadTime <= 0)
           {
               try
               {
                this.unload();
               }
               catch(Exception e){ System.out.println(e.getMessage()); }
               
               //this.currentPlatform.getAGV(this.getPosition());
               this.status = Status.WAITING;
               readyForNextContainer = true;
               //new load
           }
       }
       
    }
    


    

    
    @Override
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}
    
}
