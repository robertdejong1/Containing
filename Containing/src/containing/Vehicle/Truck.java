/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Truck extends ExternVehicle{
    static int capicity = 1;
    protected int timeCounter = 0;
    
    public Truck(Date arrivalDate, float arrivalTime, Platform platform, String company){ 
        super(capicity, arrivalDate, arrivalTime, new Container[1][1][1], platform, company); //true if vehicle comes to load, otherwise false
        
    
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);} //=add
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
  
    @Override
    public int getMaxSpeedUnloaded(){return 30;}
    @Override
    public int getMaxSpeedLoaded(){return 15;}
    
     public void update(){
        timeCounter++;
    }
}
