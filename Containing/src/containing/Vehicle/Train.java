/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Train extends ExternVehicle{
    static int capicity = 10000;
    protected int timeCounter = 0;
    
    public Train(Date arrivalDate, float arrivalTime, Container container){ 
        super(capicity, arrivalDate, arrivalTime, container); //true if vehicle comes to load, otherwise false
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);} //=add
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
     public void update(){
        timeCounter++;
    }
}
