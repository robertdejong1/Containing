package containing.Platform;

import containing.Dimension2f;
import containing.ParkingSpot.SeashipSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.SeashipCrane;

public class SeashipPlatform extends Platform {
    
    private final int NR_EXT_VEHICLES = 2;      // amount of external vehicles fit in the platform
    private final float CRANE_Z_POSITION = 50;  // the Z position of the cranes (where the Rails are ;))
    private final int NR_CRANES = 10;           // amount of cranes on this platform
    private final float WIDTH = 100;            // the width of this platform
    private final float LENGTH = 100;           // the length of thus platform
    
    /**
     * Create Seaship platform
     * @param position the position in the port
     */
    public SeashipPlatform(Vector3f position) {
        // initialize platform
        super(position);
        
        // set dimensions and positions of platform
        Dimension2f newDimension = new Dimension2f(WIDTH, LENGTH);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,0);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize parkingspots for barges
        vehicleSpots = new SeashipSpot[NR_EXT_VEHICLES];
        float spotSize = getDimension().width / (float)NR_EXT_VEHICLES;
        for(int i = 0; i < vehicleSpots.length; i++) {
            float spotPosition = (i+1)*spotSize;
            Vector3f vehicleSpotPosition = new Vector3f(spotPosition, 0, getDimension().length);
            vehicleSpots[i] = new SeashipSpot(vehicleSpotPosition);
        }
        
        // initialize cranes
        cranes = new SeashipCrane[NR_CRANES];
        float margin = WIDTH / (float)NR_CRANES;
        for(int i = 0; i < cranes.length; i++) {
            Vector3f cranePosition = new Vector3f(margin*i,0,CRANE_Z_POSITION);
            cranes[i] = new SeashipCrane(cranePosition);
        }
        
        Settings.messageLog.AddMessage("Created SeashipPlatform");
    }
    
    @Override
    public void update() {
        
    }

}