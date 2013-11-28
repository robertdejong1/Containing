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
public class Barge extends ExternVehicle {
    static int capicity = 1;
    protected int timeCounter = 0;
    public static float width = 5f; //????????
    public static float length = 10f; //??????????
    
    public Barge(Date arrivalDate, float arrivalTime, Platform platform, String company){ 
        super(capicity, arrivalDate, arrivalTime, new Container[6][3][3], platform, company); //true if vehicle comes to load, otherwise false
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);} //=add
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
     public void update(){
        timeCounter++;
    }
    
    @Override
    public int getMaxSpeedUnloaded(){return 25;}
    @Override
    public int getMaxSpeedLoaded(){return 30;}
}
