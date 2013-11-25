package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;
import java.util.List;

public abstract class Vehicle 
{     
    protected boolean isLoaded;
    protected int capicity;
    protected List<Container> cargo;
    protected static int maxSpeedLoaded;
    protected static int maxSpeedUnloaded;
    
    public Vehicle(int capicity){
        this.isLoaded = cargo.isEmpty() ? false : true;
        this.capicity = capicity;
        this.cargo = null;       
    }
    
    public void load(Container container){
        if (cargo.isEmpty()) isLoaded = true;
        if (cargo.size() < capicity) cargo.add(container);
        else {throw new IndexOutOfBoundsException(String.format("Vehicle has reached capicity, container: {0} can't be loaded to vehicle.", container.getContainerId()));}
        
    } 
 
   
    
    
    
    

}
