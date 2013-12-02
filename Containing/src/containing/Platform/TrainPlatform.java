package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.BargeSpot;
import containing.ParkingSpot.TrainSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.TrainCrane;

/**
 * TrainPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TrainPlatform extends Platform {
    
    private final float WIDTH          = 100f;  // ???
    private final float LENGTH         = 1450f; // ???
    private final int MAX_VEHICLES     = 1;
    private final int CRANES           = 4;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 20f;   // ???
    private final float VEHICLE_OFFSET = 0f;
    
    public TrainPlatform(Vector3f position)
    {
        super(position);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(WIDTH,0,0));
        setExitpoint(new Vector3f(WIDTH,0,LENGTH));
        setTransportType(TransportType.Train);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(CRANE_OFFSET /* + TrainCrane.length */ + AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        log("Created TrainPlatform object: " + toString());
    }

    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( /*TrainCrane.width*/ 5f / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 0, space*i + offset);
            cranes.add(new TrainCrane(cranePosition, this));
        }
    }
    
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        float offset = (space / 2f) - (TrainSpot.length / 2f);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(VEHICLE_OFFSET,0,space*i + offset);
            extVehicleSpots.add(new BargeSpot(spotPosition));
        }
    }
    
    @Override
    public void update()
    {
        time += Settings.ClockDelay;
        requestNextJob();
    }
    
}