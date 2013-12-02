package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.BargeSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.BargeCrane;

/**
 * BargePlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class BargePlatform extends Platform {
    
    private final float WIDTH          = 100f;  // ???
    private final float LENGTH         = 725f;  // ???
    private final int MAX_VEHICLES     = 2;
    private final int CRANES           = 8;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 98f;   // ???
    private final float VEHICLE_OFFSET = 0f;
    
    public BargePlatform(Vector3f position)
    {
        super(position);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(0,0,0));
        setExitpoint(new Vector3f(0,0,LENGTH));
        setTransportType(TransportType.Barge);
        createAgvSpots(new Vector3f(CRANE_OFFSET /* - BargeCrane.length */ - AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        log("Created BargePlatform object: " + toString());
    }
    
    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( /*BargeCrane.width*/ 5f / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 0, space*i + offset);
            cranes.add(new BargeCrane(cranePosition, this));
        }
    }

    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        float offset = (space / 2) - (BargeSpot.length / 2);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(WIDTH + VEHICLE_OFFSET,0,space*i + offset);
            extVehicleSpots.add(new BargeSpot(spotPosition));
        }
    }
    
    @Override
    public void update()
    {
        time += Settings.ClockDelay;
    }
    
}