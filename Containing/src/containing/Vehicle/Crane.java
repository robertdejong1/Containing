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
public abstract class Crane extends InternVehicle {
    
    protected int timeCounter = 0;
    protected final static int CAPICITY = 1;
    protected int counter;
    protected final float SECURETIME = 0.5f;
    
    //testvariables -> for every crane and container different
    private final int liftTimeMin = 0;
    private final float liftTimeMax = 3.5f * 60;
    private final float dropTimeMin = 0;
    private final float dropTimeMax = 3.5f * 60;
    private final float moveContainerSpeed = 5;
    protected final int maxSpeedUnloaded = 4;
    private final int resetTime = 50;
    protected final int metersToNextAgvSpot = 10;
    
    public float width;
    public float length;
    
    public Crane(Vector3f startPosition, Platform platform, Type type, float width, float length){
        super(CAPICITY, startPosition, platform, type);
        this.width = width;
        this.length = length;
    }
    
    public void unload(AGV agv) throws VehicleOverflowException, CargoOutOfBoundsException{
        try{
        agv.load(super.unload());
        }
        catch(Exception e){throw e;}
        //platform moet agv volgende route geven
    }

    
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException{ //container from extern verhicle
        try{
        super.load(container);
        }
        catch(Exception e){throw e;}
        //update: while (this.timeCounter < starttime + liftTimeMax + moveContainerSpeed * 2){} //aan het laden
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
    public void reset(){
        /*
        int starttime = this.timeCounter;
        while (this.timeCounter < starttime + resetTime){} //kraan gaat t
        //evt ga opzij [moet platform doen]
        this.isAvailable = true;
        this.timeCounter = 0;
        */
    }
    
    
    
    //methode opzij in subclasses waarvoor geldig is
}
