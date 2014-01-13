package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.TruckSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.TruckCrane;

/**
 * TruckPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TruckPlatform extends Platform {
    
    private final float WIDTH          = 100f*Settings.METER;  // ???
    private final float LENGTH         = 821.5f*Settings.METER;  // ???
    private final int MAX_VEHICLES     = 20;    // ???
    public final int CRANES           = 20;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 80f;   // ???
    private final float VEHICLE_OFFSET = 0f;
    
    public TruckPlatform(Vector3f position)
    {
        super(position, Platform.Positie.RECHTS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(Platform.DynamicAxis.Z);
        setEntrypoint(new Vector3f(0,0,0));
        setExitpoint(new Vector3f(0,0,LENGTH));
        setRoad();
        setTransportType(TransportType.Truck);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(CRANE_OFFSET - TruckCrane.length - AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        log("Created TruckPlatform object: " + toString());
    }

    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( TruckCrane.width / 2f) + getPosition().z;
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 5.5f, space*i + offset);
            cranes.add(new TruckCrane(cranePosition, this));
        }
    }
    
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        float offset = (space / 2) - (TruckSpot.width / 2);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(WIDTH + VEHICLE_OFFSET,0,space*i + offset);
            extVehicleSpots.add(new TruckSpot(spotPosition));
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        
        /* if platform is free, request next job */
        if(state.equals(State.FREE))
            requestNextJob();
        
        if(jobs.size() > 0)
            state = State.LOAD;
        else if(hasExtVehicle())
            state = State.UNLOAD;
        else
            state = State.FREE;
        
        /* UNLOAD EXTERNAL VEHICLE */
        if(state.equals(State.UNLOAD))
        {
            unload();
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            load(this);
        }
    }
    
}