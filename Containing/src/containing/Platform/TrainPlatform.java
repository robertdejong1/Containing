package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.TrainSpot;
import containing.Vehicle.TrainCrane;
import java.util.Arrays;

public class TrainPlatform extends Platform {
    
    private final int NR_TRAINS = 1;
    private final ParkingSpot[] trainSpots;

    public TrainPlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize train spots
        trainSpots = new TrainSpot[NR_TRAINS];
        Arrays.fill(trainSpots, new TrainSpot());
        // initialize cranes
        Arrays.fill(cranes, new TrainCrane());
    }
    
    @Override
    public void update() {
        
    }

}