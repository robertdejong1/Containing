package containing.Platform;

import containing.Vehicle.BargeCrane;
import java.util.Arrays;

public class BargePlatform extends Platform {

    public BargePlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize cranes
        Arrays.fill(cranes, new BargeCrane());
    }
    
}