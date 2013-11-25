package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.SeashipSpot;
import containing.Vector3f;

public class SeashipPlatform extends Platform {
    
    private final int NR_SEASHIPS = 2;
    private final ParkingSpot[] seashipSpots;

    public SeashipPlatform(Vector3f position) {
        super(position);
        // initialize seaship parking spots
        seashipSpots = new SeashipSpot[NR_SEASHIPS];
        //Arrays.fill(seashipSpots, new SeashipSpot());
        // initialize cranes
        //Arrays.fill(cranes, new SeashipCrane());
    }
    
    @Override
    public void update() {
        
    }

}