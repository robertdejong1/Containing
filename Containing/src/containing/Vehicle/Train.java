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
import java.util.Date;

/**
 *
 * @author Robert
 */
public class Train extends ExternVehicle{
    static int capicity = 10000;
    protected int timeCounter = 0;
    public static float width = 5f; //????????
    public static float length = 30f; //??????????
    public Train(Date arrivalDate, float arrivalTime, Platform platform, String company){          //rij hoogte kolom
        super(capicity, arrivalDate, arrivalTime,  new Container[17][1][1], platform, company, Type.TRAIN); //true if vehicle comes to load, otherwise false
       
    }
    
    public Container unload() throws CargoOutOfBoundsException{
        try{return super.unload();}
        catch(Exception e){throw e;}
    }
    
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException{
        try{super.load(container);}
        catch(Exception e){throw e;}
    } //=add
    
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
