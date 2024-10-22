package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.SeashipSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.SeashipCrane;

/**
 * SeashipPlatform.java
 * The dynamic axis for cranes is: X
 * @author Minardus
 */
public class SeashipPlatform extends Platform {
    
    public final float WIDTH          = 100f *Settings.METER; // ???
    public final float LENGTH         = 855f *Settings.METER; // ???
    private final int MAX_VEHICLES     = 2;
    public final int CRANES           = 10;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 8f; //*Settings.METER;  // ???
    private final float VEHICLE_OFFSET = 0f;
    
    /**
     * Create trainplatform
     * @param position the position in the Port
     */
    public SeashipPlatform(Vector3f position)
    {
        super(position, Positie.ONDER);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.X);
        setEntrypoint(new Vector3f(WIDTH,0,0));
        setExitpoint(new Vector3f(0,0,0));
        setRoad();
        setTransportType(TransportType.Seaship);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(0, 0, CRANE_OFFSET + SeashipCrane.length + AGV_OFFSET));
        createExtVehicleSpots();
        createCranes();
        log("Created SeashipPlatform object: " + toString());
    }
    
    /**
     * Create cranes
     */
    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( SeashipCrane.width / 2f) + getPosition().x;
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(space*i + offset, 5.5f, CRANE_OFFSET + getPosition().z);
            cranes.add(new SeashipCrane(cranePosition, this));
        }
    }
    
    /**
     * Create parking spots for Extern Vehicles
     */
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = WIDTH / (float)MAX_VEHICLES;
        float offset = (space / 2) - (SeashipSpot.length / 2);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(space*i + offset,0,-VEHICLE_OFFSET);
            extVehicleSpots.add(new SeashipSpot(spotPosition));
        }
    }
    
    /**
     * Update is called every 100ms
     */
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