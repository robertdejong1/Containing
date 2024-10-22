/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Command;
import containing.CommandHandler;
import containing.Container;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Settings;
import containing.Vector3f;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Robert
 */
public abstract class InternVehicle extends Vehicle{
    
    protected boolean isAvailable;
    protected Vector3f startPosition;
    
    public InternVehicle(int capicity, Vector3f startPosition, Platform platform, Type type){
        super(capicity, platform, type);

        this.startPosition = startPosition;
        this.setPosition(startPosition);
        this.position = startPosition;
        this.isAvailable = true;
    }
    
   public boolean getIsAvailable()
   {
       return this.isAvailable;
   }
   
   public void setIsAvailable(boolean isAvailable) {
       this.isAvailable = isAvailable;
   }
    
   @Override
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException {
        try
        {
         
            super.load(container);
            if (isAvailable) isAvailable = false;
            
 
        }
        
        catch (Exception e){ throw e; }

    }
    
    public Container unload() throws ContainerNotFoundException
    { 
        if(cargo == null)
            cargo = new ArrayList<>();
        if (!cargo.isEmpty())
        {
            Container container = cargo.get(0);
            
            if (this.getVehicleType() ==  Type.AGV)
            {
                this.isAvailable = true;
            }
           
            
            this.cargo = new ArrayList<>();
            
            this.isLoaded = false;
            /*
            if (this.getVehicleType() ==  Type.AGV){
                
                HashMap<String, Object> map = new HashMap<>();

                map.put("id", this.getID());
                map.put("vehicleType", this.getVehicleType());
                map.put("container", cargo.get(0)); 

                CommandHandler.addCommand(new Command("unloadVehicle", this));
            }*/
           
            return container;
            
        }
        
        else { throw new ContainerNotFoundException("No container to unload"); }
        
    }
    
  
    
    
}
