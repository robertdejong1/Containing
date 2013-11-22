package containing.Platform;

import containing.Vehicle.TrainCrane;
import java.util.Arrays;

public class TrainPlatform extends Platform {

    public TrainPlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize cranes
        Arrays.fill(cranes, new TrainCrane());
    }

}