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
import containing.Platform.Platform;
import containing.Point3D;
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
    private float unloadtime = 0;
    private float loadtime = 0;
    Point3D containerindex = null;
    //hier StorageCrane specific variables
    private Vector3f containerStoragePosition = null;
    private Vector3f defaultPositionStorageStrip = null;
    public StorageCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform, Type.STORAGECRANE, width, length);

    }
    
   
    public void load(AGV agv, Vector3f containerStoragePosition, int i, Vector3f defaultPositionStorageStrip, Point3D containerindex) throws VehicleOverflowException, CargoOutOfBoundsException, Exception{ //container from extern verhicle
    try
        {
            this.containerindex = containerindex;
            System.out.println("StorageIII: " + i);
            System.out.println("containerStoragePosition: " + containerStoragePosition);
            this.containerStoragePosition = containerStoragePosition;
            this.defaultPositionStorageStrip =  defaultPositionStorageStrip;
            //this.position = defaultPositionStorageStrip;
            List<Vector3f> path = new ArrayList<Vector3f>();
            
            super.load(agv.unload());
            System.out.println("StorageCraneAGV: " + agv.getStatus());
            
            this.getCargo().get(0).setArrivalPosition(agv.getPosition());
            
            this.loadtime = 20 * 10;  
            this.unloadtime = 25 * 10;
            path.add(this.position);
            path.add(defaultPositionStorageStrip);
            this.position = defaultPositionStorageStrip;
            this.status = Status.LOADING;
            
            System.out.println("StorageCraneStatus: " + this.status);
            /*
            path.add(agv.getPosition());
            path.add(this.position);
            path.add(containerStoragePosition);
            path.add(this.position);*/
       
            HashMap<String, Object> map = new HashMap<>();
            
             map.put("craneid", this.getID());
             map.put("vehicleType", this.getVehicleType());
             map.put("clientid", agv.getID());
             map.put("duration", this.loadtime);
             
            
          
             map.put("indexnr", i);

             map.put("path", path);

             CommandHandler.addCommand(new Command("loadStorageCrane", map));
           
        }
        catch(Exception e)
        {
            System.out.println("ERRORERROR");
            throw e;
        }

        //this.timeCountDown = (int) liftTimeMax;
        //update: while (this.timeCounter < starttime + liftTimeMax + moveContainerSpeed * 2){} //aan het laden
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
    //from agvspot to containerStoragePosition
    public void unload(Vector3f containerStoragePosition)
    {
        Container container = super.unload();
        this.unloadtime = 25 * 10;
        List<Vector3f> path = new ArrayList<Vector3f>();
        path.add(this.position); 
        path.add(this.containerStoragePosition);
        
        this.position = containerStoragePosition;
        for (Vector3f v : path)
        {
            System.out.println("StorageCraneUnloadPath: " + v );
        } 
        System.out.println("DURATIONUNLOAD: " + this.unloadtime);
        container.setArrivalPosition(containerStoragePosition); //update container positie
        HashMap<String, Object> map = new HashMap<>();
            
             map.put("craneid", this.getID());
             map.put("vehicleType", this.getVehicleType());
          
             map.put("duration", this.unloadtime);
             map.put("containerindex", containerindex);
             map.put("path", path);

             CommandHandler.addCommand(new Command("unloadStorageCrane", map));
        
    }
    
     @Override
    public void update()
    {
        //System.out.println("IN STORAGE CRANE UPDATE");
       //super.update();
       
       if (this.status == Status.LOADING )
       {
           //System.out.println("UpdateLoading");
           this.loadtime--;
           if (this.loadtime <= 0)
           { 
               
               this.status = Status.UNLOADING;
               try
               {
                this.unload(containerStoragePosition);
               }
               catch(Exception e){ System.out.println(e.getMessage()); }
               
               //command simulator
           }
           
       }
       
       if (this.status == Status.UNLOADING)
       {
           this.unloadtime--;
           //System.out.println("UpdateUnloading");
           if (this.unloadtime <= 0)
           {   
               //System.out.println("FINISHFINSIHSTORAGECRANE");
      
               
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
