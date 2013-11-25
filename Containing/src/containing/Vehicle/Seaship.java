/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Container;
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
    
    public Seaship(Date arrivalDate, float arrivalTime){
        super(capicity, arrivalDate, arrivalTime); //true if vehicle comes to load, otherwise false
        SeashipId = counter;
        counter++;
    }
   
    public Container unload(){return super.unload();}
    
    public void load(Container container){
            super.load(container);
    } //=add
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
    
  
 
}
