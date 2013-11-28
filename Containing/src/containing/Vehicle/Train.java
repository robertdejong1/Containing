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
public class Train extends ExternVehicle{
    static int capicity = 10000;
    protected int timeCounter = 0;
    
    public Train(Date arrivalDate, float arrivalTime, Platform platform, String company){          //rij hoogte kolom
        super(capicity, arrivalDate, arrivalTime,  new Container[1][2][1], platform, company); //true if vehicle comes to load, otherwise false
       
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);} //=add
    
    public void leave(){super.leave();}
    public void enter(){super.enter();}
    
    @Override
    public int getMaxSpeedUnloaded(){return 20;}
    @Override
    public int getMaxSpeedLoaded(){return 10;}
    
     public void update(){
        timeCounter++;
    }
}
