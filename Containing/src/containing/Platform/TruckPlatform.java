package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.ParkingSpot.TruckSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.TruckCrane;

/**
 * StoragePlatform
 * 
 * Crane movement:
 * X-axis: static
 * Z-axis: dynamic
 * 
 * @author Minardus
 */
public class TruckPlatform extends Platform {
    
    private final int NR_EXT_VEHICLES = 1;      // amount of external vehicles fit in the platform
    private final float CRANE_X_POSITION = 50;  // the Z position of the cranes (where the Rails are ;))
    private final int NR_CRANES = 20;           // amount of cranes on this platform
    private final float X_SIZE = 100;            // the width of this platform
    private final float Z_SIZE = 100;           // the length of thus platform
    
    /**
     * Create Truck platform
     * @param position the position in the port
     */
    public TruckPlatform(Vector3f position) {
        // initialize platform
        super(position);
        
        // set dimensions and entrance/exit waypoints of platform
        Dimension2f newDimension = new Dimension2f(X_SIZE, Z_SIZE);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,Z_SIZE);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize AGV spots
        initAgvSpots('z');
        
        // initialize parkingspots for barges
        initVehicleSpots();
        
        // initialize cranes
        initCranes();
        
        Settings.messageLog.AddMessage("Created TruckPlatform: " + toString());
    }

    @Override
    protected final void initVehicleSpots() {
        vehicleSpots = new TruckSpot[NR_EXT_VEHICLES];
        float spotSize = Z_SIZE / (float)NR_EXT_VEHICLES;
        for(int i = 0; i < vehicleSpots.length; i++) {
            float spotPosition = (i+1)*spotSize;
            Vector3f vehicleSpotPosition = new Vector3f(X_SIZE + TruckSpot.width, 0, spotPosition);
            vehicleSpots[i] = new TruckSpot(vehicleSpotPosition);
        }
    }

    @Override
    protected final void initCranes() {
        cranes = new TruckCrane[NR_CRANES];
        float margin = X_SIZE / (float)NR_CRANES;
        for(int i = 0; i < cranes.length; i++) {
            Vector3f cranePosition = new Vector3f(CRANE_X_POSITION, 0, margin*i);
            cranes[i] = new TruckCrane(cranePosition, this);
        }
    }
    
    @Override
    public Container.TransportType getTransportType() {
        return Container.TransportType.Truck;
    }
    
    @Override
    public void update() {
        
    }
    
}