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
public class Seaship extends ExternVehicle {
    private static int counter;
    private int SeashipId;
    static int capicity = 10000;
    int[][][] grid;
    public static float width = 10f; //????????
    public static float length = 20f; //??????????
    
    public Seaship(Date arrivalDate, float arrivalTime, Platform platform, String company){
        super(capicity, arrivalDate, arrivalTime, new Container[20][16][6], platform, company); //true if vehicle comes to load, otherwise false
        SeashipId = counter;
        counter++;
     
    }
   
    public Container unload(){return super.unload();}
    
    public void load(Container container){
            super.load(container);
    } //=add
    
    @Override
    public int getMaxSpeedUnloaded(){return 15;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
    
  
 
}
