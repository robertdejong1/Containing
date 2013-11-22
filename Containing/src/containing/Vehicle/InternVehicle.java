/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import java.util.List;

/**
 *
 * @author Robert
 */
public abstract class InternVehicle extends Vehicle{
    
    protected boolean isAvailable;
    
    public InternVehicle(int capicity, List<Container> cargo){
        super(capicity, cargo);
        isAvailable = false;
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
        return null;
    }
    
    
}
