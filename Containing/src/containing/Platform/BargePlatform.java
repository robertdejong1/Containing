package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.BargeSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.BargeCrane;

/**
 * StoragePlatform
 * 
 * Crane movement:
 * X-axis: static
 * Z-axis: dynamic
 * 
 * @author Minardus
 */
public class BargePlatform extends Platform {
    
    private final int NR_EXT_VEHICLES = 2;      // amount of external vehicles fit in the platform
    private final float CRANE_X_POSITION = 50;  // the Z position of the cranes (where the Rails are ;))
    private final int NR_CRANES = 8;            // amount of cranes on this platform
    private final float X_SIZE = 100;            // the width of this platform
    private final float Z_SIZE = 100;           // the length of thus platform
    
    /**
     * Create Barge platform
     * @param position the position in the port
     */
    public BargePlatform(Vector3f position) {
        // initialize platform
        super(position);
        
        // set dimensions and entrance/exit waypoints of platform
        Dimension2f newDimension = new Dimension2f(X_SIZE, Z_SIZE);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,0);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize vehicle spots
        initVehicleSpots();
        
        // initialize cranes
        initCranes();
        
        Settings.messageLog.AddMessage("Created BargePlatform object: " + toString());
    }
    
    @Override
    protected final void initVehicleSpots() {
        vehicleSpots = new BargeSpot[NR_EXT_VEHICLES];
        float spotSize = Z_SIZE / (float)NR_EXT_VEHICLES;
        for(int i = 0; i < vehicleSpots.length; i++) {
            float spotPosition = (i+1)*spotSize;
            Vector3f vehicleSpotPosition = new Vector3f(X_SIZE + BargeSpot.width, 0, spotPosition);
            vehicleSpots[i] = new BargeSpot(vehicleSpotPosition);
        }
    }
    
    @Override
    protected final void initCranes() {
        cranes = new BargeCrane[NR_CRANES];
        float margin = Z_SIZE / NR_CRANES;
        for(int i = 0; i < cranes.length; i++) {
            Vector3f cranePosition = new Vector3f(CRANE_X_POSITION, 0, margin*i);
            cranes[i] = new BargeCrane(cranePosition);
        }
    }
    
    @Override
    public TransportType getTransportType() {
        return TransportType.Barge;
    }
    
    @Override
    public void update() {
        
    }
    
}