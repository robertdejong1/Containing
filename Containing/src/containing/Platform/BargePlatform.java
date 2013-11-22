package containing.Platform;

import containing.ParkingSpot.BargeSpot;
import containing.ParkingSpot.ParkingSpot;
import containing.Vehicle.BargeCrane;
import java.util.Arrays;

public class BargePlatform extends Platform {
    
    private final int NR_BARGES = 2;
    private final ParkingSpot[] bargeSpots;

    public BargePlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize parkingspots for barges
        bargeSpots = new BargeSpot[NR_BARGES];
        Arrays.fill(bargeSpots, new BargeSpot());
        // initialize cranes
        Arrays.fill(cranes, new BargeCrane());
    }
    
    @Override
    public void update() {
        
    }
    
}