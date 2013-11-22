/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Robert
 */
public abstract class Crane extends InternVehicle {

    protected static int capicityCrane = 1;
    
    public Crane(){
        super(capicityCrane, null);
    }
    
    public void unload(AGV agv){
        agv.load(super.unload());
        //platform moet agv volgende route geven
    }

    public void load(Container container, int pickAndDropTime, int resetTime){ //container from extern verhicle
        super.load(container);
        try{  
            TimeUnit.SECONDS.sleep(pickAndDropTime);
           }
        catch(InterruptedException ie){}
        //roep evt nieuwe agv aan (op zelfde parkeerplaats of op parkeerplaats opzij [moet platform doen]
    }
    
    public void reset(int resetTime){
        try{  
            TimeUnit.SECONDS.sleep(resetTime);
           }
        catch(InterruptedException ie){}
        //evt ga opzij [moet platform doen]
        this.isAvailable = true;
    }
    
    //methode opzij in subclasses waarvoor geldig is
}
