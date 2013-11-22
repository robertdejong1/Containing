package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.SeashipSpot;
import containing.Vehicle.SeashipCrane;
import java.util.Arrays;

public class SeashipPlatform extends Platform {
    
    private final int NR_SEASHIPS = 2;
    private final ParkingSpot[] seashipSpots;

    public SeashipPlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize seaship parking spots
        seashipSpots = new SeashipSpot[NR_SEASHIPS];
        Arrays.fill(seashipSpots, new SeashipSpot());
        // initialize cranes
        Arrays.fill(cranes, new SeashipCrane());
    }
    
    @Override
    public void update() {
        
    }

}