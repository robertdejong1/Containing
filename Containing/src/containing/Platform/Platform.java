package containing.Platform;

import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.AgvSpot;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import java.util.Arrays;

public abstract class Platform {
    
    protected final int id;
    protected final AgvSpot[] agvSpots;
    protected final Crane[] cranes;
    
    public Platform(int id, int nrAgvSpots, int nrCranes) {
        this.id = id;
        // initialize parking spots for AGV's
        agvSpots = new AgvSpot[nrAgvSpots];
        Arrays.fill(agvSpots, new AgvSpot());
        // declare cranes
        cranes = new Crane[nrCranes];
    }
    
    /*
    public void parkAGV(AGV agv) {
        for(AgvSpot spot : agvSpots) {
            if(spot.isFree()) {
                spot.parkAGV(agv);
            }    
        }
    }
    */
    
    public ParkingSpot getAgvSpot(int spot) {
        return agvSpots[spot];
    }
    
    public int getId() {
        return id;
    }
    
    public abstract void update();
    
}
