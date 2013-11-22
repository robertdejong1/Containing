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
public abstract class ExternVehicle extends Vehicle {
    
    
    public ExternVehicle(int capicity, List<Container> cargo){super(capicity, cargo);}
    
    public void load(Container container){}
    
    public Container unload(){
        if (cargo.isEmpty()) return null;
        Container container = cargo.get(0);
        cargo.remove(container);
        return container;
    }
    //public void leave(ExternVehicle vehicle){}
}
