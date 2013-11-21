package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.AgvSpot;

public class Platform {
    
    private int id;
    private ParkingSpot[] agvSpots;
    
    public Platform(int id, int nrAvgSpots) {
        this.id = id;
        initAvgSpots(nrAvgSpots);
    }
    
    private void initAvgSpots(int nrAvgSpots) {
        agvSpots = new ParkingSpot[nrAvgSpots];
        for(int i = 0; i < nrAvgSpots; i++)
            agvSpots[i] = new AgvSpot();
    }
    
    public int getId() {
        return id;
    }
    
}
