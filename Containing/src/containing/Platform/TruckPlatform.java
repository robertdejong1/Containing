package containing.Platform;

import containing.Vector3f;
import containing.Vehicle.TruckCrane;
import java.util.Arrays;

public class TruckPlatform extends Platform {

    public TruckPlatform(Vector3f position) {
        super(position);
        Arrays.fill(cranes, new TruckCrane()); //initialize cranes
    }
    
    @Override
    public void update() {
        
    }
    
}