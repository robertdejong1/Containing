package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.AgvSpot;
import containing.Vehicle.Crane;
import java.util.Arrays;

public class Platform {
    
    protected final int id;
    protected final ParkingSpot[] agvSpots;
    protected final Crane[] cranes;
    
    public Platform(int id, int nrAgvSpots, int nrCranes) {
        this.id = id;
        // initialize parking spots for AGV's
        agvSpots = new ParkingSpot[nrAgvSpots];
        Arrays.fill(agvSpots, new AgvSpot());
        // declare cranes
        cranes = new Crane[nrCranes];
    }
    
    public ParkingSpot getAgvSpot(int spot) {
        return agvSpots[spot];
    }
    
    public int getId() {
        return id;
    }
    
}
