/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
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
    
    public Crane(Vector3f startPosition){
        super(CAPICITY, startPosition);
    }
    
    public void unload(AGV agv){
        int starttime = this.timeCounter;
        while (this.timeCounter < starttime + dropTimeMax){}
        agv.load(super.unload());
        //platform moet agv volgende route geven
    }

    
    public void load(Container container){ //container from extern verhicle
        int starttime = this.timeCounter;
        super.load(container);
        while (this.timeCounter < starttime + liftTimeMax + moveContainerSpeed * 2){} //aan het laden
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
    public void reset(){
        int starttime = this.timeCounter;
        while (this.timeCounter < starttime + resetTime){} //kraan gaat t
        //evt ga opzij [moet platform doen]
        this.isAvailable = true;
        this.timeCounter = 0;
    }
    
    
    
    //methode opzij in subclasses waarvoor geldig is
}
