package containing.Platform;

import containing.Vehicle.SeashipCrane;
import java.util.Arrays;

public class SeashipPlatform extends Platform {

    public SeashipPlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize cranes
        Arrays.fill(cranes, new SeashipCrane());
    }

}