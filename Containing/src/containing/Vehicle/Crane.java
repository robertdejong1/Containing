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
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.VehicleOverflowException;
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
public abstract class Crane extends InternVehicle {
    
    protected int timeCountDown = 0;
    protected final static int CAPICITY = 1;
    protected int counter;
    protected final float SECURETIME = 0.5f * 60;
    
    //testvariables -> for every crane and container different
    private final int liftTimeMin = 0 * 60;
    private final float liftTimeMax = 3.5f * 60;
    private final float dropTimeMin = 0 * 60;
    private final float dropTimeMax = 3.5f * 60;
    private final float moveContainerSpeed = 5; //meter per seconde
    protected final int maxSpeedUnloaded = 4;
    protected final int maxSpeedLoaded = 5;
    
    private float loadTime;
    private float unloadTime;
    private float resetTime;
    
    private AGV agvToUnload = null;
    
    boolean readyForNextContainer = true;

    protected final int metersToNextAgvSpot = 10;
    
    public float width;
    public float length;
    
    

    public Crane(Vector3f startPosition, Platform platform, Type type, float width, float length){
        super(CAPICITY, startPosition, platform, type);
        this.width = width;
        this.length = length;

    }
    
    public void unload(AGV agv) throws VehicleOverflowException, ContainerNotFoundException, CargoOutOfBoundsException{
        try
        {
            agv.load(super.unload());
        }
        catch(Exception e){ throw e; }
        //platform moet agv volgende route geven
    }
    

    public void load(ExternVehicle ev, int column) throws Exception
    {
        for (Integer row : ev.getUnloadOrderY(column))
        {
                for (Container container : ev.getGrid()[column][row])
                {
                    while (readyForNextContainer)
                    try
                    {
                        if (container != null)
                        {
                            readyForNextContainer = false;
                            //ask agv!
                            super.load(ev.unload(container));
                            
                            this.loadTime = (Math.abs(this.position.y-container.getArrivalPosition().y) / this.moveContainerSpeed //move gripper to position of container
                            //+ this.dropTimeMin + (this.dropTimeMax - this.dropTimeMin) / ((int)container.getArrivalPosition().z + 1) //droptime depended on z position of container //???
                            + this.SECURETIME
                            + this.liftTimeMin + (this.liftTimeMax - this.liftTimeMin) / ((int)container.getArrivalPosition().z + 1) //lifttime depended on z position of container
                            + Math.abs(this.position.y-container.getArrivalPosition().y) / this.moveContainerSpeed) * 100;
                            this.status = Status.LOADING;
                            
                            this.unloadTime = (this.dropTimeMin + (this.dropTimeMax - this.dropTimeMin) / ((int)container.getArrivalPosition().z + 1) + this.SECURETIME) * 100;
                            
                             
                        }
                    }
                    catch(Exception e)
                    {
                        throw e;
                    }
                    
                }
            
       
        }
        
        this.isAvailable = true;
    }
    
    

    
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException{ //container from extern verhicle
        try
        {
            super.load(container);
            this.loadTime = (Math.abs(this.position.y-container.getArrivalPosition().y) / this.moveContainerSpeed //move gripper to position of container
                            //+ this.dropTimeMin + (this.dropTimeMax - this.dropTimeMin) / ((int)container.getArrivalPosition().z + 1) //droptime depended on z position of container
                            + this.SECURETIME
                            + this.liftTimeMin + (this.liftTimeMax - this.liftTimeMin) / ((int)container.getArrivalPosition().z + 1) //lifttime depended on z position of container
                            + Math.abs(this.position.y-container.getArrivalPosition().y) / this.moveContainerSpeed) * 100;
            
            this.status = Status.LOADING;
            
            this.unloadTime = (this.dropTimeMin + (this.dropTimeMax - this.dropTimeMin) / ((int)container.getArrivalPosition().z + 1) + this.SECURETIME) * 100;
        }
        catch(Exception e)
        {
            throw e;
        }

        //this.timeCountDown = (int) liftTimeMax;
        //update: while (this.timeCounter < starttime + liftTimeMax + moveContainerSpeed * 2){} //aan het laden
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
    
    
    public void move(int direction) //-1 is left 1 is right
    {
        
        //override this method for truckcrane
        List<Vector3f> route = new ArrayList<>();
        route.add(this.position);
        if (direction == -1)
        {
            route.add(new Vector3f(this.position.x - Container.depth, this.position.y, this.position.z));
        }
        
        else 
        {
            route.add(new Vector3f(this.position.x + Container.depth, this.position.y, this.position.z));
        }
        
        this.currentSpeed = (this.isLoaded) ? this.maxSpeedLoaded : this.maxSpeedUnloaded;
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", this.getID());
        map.put("vehicleType", this.getVehicleType());
        map.put("motionPath", route); 
        map.put("speed", currentSpeed);
        
        this.route = new Route(route,Road.getPathLength(route));
            
        CommandHandler.addCommand(new Command("moveCrane", map));
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
       
       if (this.status == Status.UNLOADING && this.agvToUnload != null)
       {
           this.unloadTime--;
           if (this.unloadTime <= 0)
           {
               try
               {
                this.unload(agvToUnload);
               }
               catch(Exception e){ System.out.println(e.getMessage()); }
               this.agvToUnload = null;
               //this.currentPlatform.getAGV(this.getPosition());
               this.status = Status.WAITING;
               readyForNextContainer = true;
               //new load
           }
       }
       
    }
    
    public void setAGV(AGV agv){ this.agvToUnload = agv; }
    

 
    

    
    
    
    //methode opzij in subclasses waarvoor geldig is
}
