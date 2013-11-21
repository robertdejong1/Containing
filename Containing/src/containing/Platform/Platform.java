package containing.Platform;

import containing.ParkingSpot.ParkingSpot;

public class Platform {
    
    private int id;
    private ParkingSpot[] avgSpots;
    
    public Platform(int id, int nrAvgSpots) {
        this.id = id;
        initAvgSpots(nrAvgSpots);
    }
    
    private void initAvgSpots(int nrAvgSpots) {
        avgSpots = new ParkingSpot[nrAvgSpots];
        for(int i = 0; i < nrAvgSpots; i++)
            avgSpots[i] = new AvgSpot();
    }
    
    public int getId() {
        return id;
    }
    
}
