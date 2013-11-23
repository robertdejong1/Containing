/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public abstract class InternVehicle extends Vehicle{
    
    protected boolean isAvailable;
    protected Vector3f startPosition;
    
    public InternVehicle(int capicity, Vector3f startPosition){
        super(capicity);
        isAvailable = false;
        this.startPosition = startPosition;
    }
    
    protected void load(Container container){
        super.load(container);
        if (isAvailable) isAvailable = false;
    }
    
    protected Container unload(){ //exception if cargo == 0
        if (!cargo.isEmpty()){
            Container container = cargo.get(0);
            this.isAvailable = true;
            this.cargo = null;
            this.isLoaded = false;
            return container;
        }
        else {throw new IndexOutOfBoundsException(String.format("Vehicle has no cargo to unload"));}
    }
    
    
}
