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
import containing.Exceptions.VehicleOverflowException;
import containing.Platform.Platform;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public abstract class InternVehicle extends Vehicle{
    
    protected boolean isAvailable;
    protected Vector3f startPosition;
    
    public InternVehicle(int capicity, Vector3f startPosition, Platform platform, Type type){
        super(capicity, platform, type);
        isAvailable = false;
        this.startPosition = startPosition;
    }
    
    public void load(Container container) throws VehicleOverflowException, CargoOutOfBoundsException {
        try{
        super.load(container);
        if (isAvailable) isAvailable = false;
        }
        catch (Exception e){}

    }
    
    public Container unload() throws CargoOutOfBoundsException{ //exception if cargo == 0
        if (!cargo.isEmpty()){
            Container container = cargo.get(0);
            this.isAvailable = true;
            this.cargo = null;
            this.isLoaded = false;
            CommandHandler.addCommand(new Command("unloadVehicle", this));
            return container;
            
        }
        else {throw new CargoOutOfBoundsException("CargoOutOfBounds");}
        
    }
    
    public void update(){
        this.update();
        
    }
    
    
}
