package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.SeashipSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.SeashipCrane;

/**
 * StoragePlatform
 * 
 * Crane movement:
 * X-axis: dynamic
 * Z-axis: static
 * 
 * @author Minardus
 */
public class SeashipPlatform extends Platform {
    
    private final int NR_EXT_VEHICLES = 2;      // amount of external vehicles fit in the platform
    private final float CRANE_Z_POSITION = 50;  // the Z position of the cranes (where the Rails are ;))
    private final int NR_CRANES = 10;           // amount of cranes on this platform
    private final float X_SIZE = 600;            // the width of this platform
    private final float Z_SIZE = 100;           // the length of thus platform
    
    /**
     * Create Seaship platform
     * @param position the position in the port
     */
    public SeashipPlatform(Vector3f position) {
        // initialize platform
        super(position);
        
        // set dimensions and entrance/exit waypoints of platform
        Dimension2f newDimension = new Dimension2f(X_SIZE, Z_SIZE);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,0);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize AGV spots
        initAgvSpots('x');
        
        // initialize parkingspots for barges
        initVehicleSpots();
        
        // initialize cranes
        initCranes();
        
        Settings.messageLog.AddMessage("Created SeashipPlatform: " + toString());
    }
    
    @Override
    protected final void initVehicleSpots() {
        vehicleSpots = new SeashipSpot[NR_EXT_VEHICLES];
        float spotSize = X_SIZE / (float)NR_EXT_VEHICLES;
        for(int i = 0; i < vehicleSpots.length; i++) {
            float spotPosition = (i+1)*spotSize;
            Vector3f vehicleSpotPosition = new Vector3f(spotPosition, 0, 0 - SeashipSpot.length);
            vehicleSpots[i] = new SeashipSpot(vehicleSpotPosition);
        }
    }
    
    @Override
    protected final void initCranes() {
        cranes = new SeashipCrane[NR_CRANES];
        float margin = X_SIZE / (float)NR_CRANES;
        for(int i = 0; i < cranes.length; i++) {
            Vector3f cranePosition = new Vector3f(margin*i,0,CRANE_Z_POSITION);
            cranes[i] = new SeashipCrane(cranePosition);
        }
    }
    
    @Override
    public TransportType getTransportType() {
        return TransportType.Seaship;
    }
    
    @Override
    public void update() {
        
    }

}