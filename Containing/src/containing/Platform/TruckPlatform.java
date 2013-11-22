package containing.Platform;

import containing.Vehicle.TruckCrane;
import java.util.Arrays;

public class TruckPlatform extends Platform {

    public TruckPlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize cranes
        Arrays.fill(cranes, new TruckCrane());
    }
    
}