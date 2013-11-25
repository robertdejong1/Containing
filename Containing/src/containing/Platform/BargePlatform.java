package containing.Platform;

import containing.Dimension2f;
import containing.ParkingSpot.BargeSpot;
import containing.ParkingSpot.ParkingSpot;
import containing.Vector3f;

public class BargePlatform extends Platform {
    
    private final int WIDTH = 100;
    private final int LENGTH = 100;
    private final int NR_BARGES = 2;
    private final ParkingSpot[] bargeSpots;
    
    /**
     * 
     * @param position 
     */
    public BargePlatform(Vector3f position) {
        // initialize platform
        super(position);
        
        // set dimensions and positions of platform
        Dimension2f newDimension = new Dimension2f(WIDTH, LENGTH);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,0);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize parkingspots for barges
        bargeSpots = new BargeSpot[NR_BARGES];
        float spotSize = getDimension().width / (float)NR_BARGES;
        for(int i = 0; i < bargeSpots.length; i++) {
            float spotPosition = (i+1)*spotSize;
            Vector3f bargeSpotPosition = new Vector3f(spotPosition, 0, getDimension().length);
            bargeSpots[i] = new BargeSpot(bargeSpotPosition);
        }
    }
    
    @Override
    public void update() {
        
    }
    
}