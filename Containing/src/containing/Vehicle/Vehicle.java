package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;
import java.util.List;

public abstract class Vehicle 
{     
    protected boolean isLoaded;
    protected int capicity;
    protected List<Container> cargo;
    
    public Vehicle(int capicity, List<Container> cargo){
        this.isLoaded = cargo.isEmpty() ? false : true;
        this.capicity = capicity;
        this.cargo = cargo;        
    }
    
    protected void load(Container container){
        if (cargo.isEmpty()) isLoaded = true;
        if (cargo.size() < capicity) cargo.add(container);
    } 
    

}
