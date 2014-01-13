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
    
    private final float WIDTH          = 100f*Settings.METER;  // ???
    private final float LENGTH         = 821.5f*Settings.METER;  // ???
    private final int MAX_VEHICLES     = 2;
    public final int CRANES           = 8;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 81.0f;  // ???
    private final float VEHICLE_OFFSET = 0f;
    
    public BargePlatform(Vector3f position)
    {
        super(position, Positie.RECHTS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(71.5f,5.5f,155.5f));
        setExitpoint(new Vector3f(71.6f, 5.5f,78.8f));
        setRoad();
        setTransportType(TransportType.Barge);
        setMaxAgvQueue(CRANES);
        createAgvSpots(new Vector3f(CRANE_OFFSET - BargeCrane.length - AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();        
        log("Created BargePlatform object: " + toString());
    }
    
    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( BargeCrane.width / 2f) + super.getPosition().z;
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(CRANE_OFFSET, 5.5f, space*i + offset);
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
    public void unload() 
    {
        super.unload();
        
        //If we have no extVehicles, platform is free
        if(extVehicles.isEmpty()){
            state = State.FREE;
            return;
        }
        
        
    }
    
    @Override
    public void update()
    {
        super.update();
        time++;
        
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
            if(time >= 10) {
                //System.out.println("unloading...");
                unload();
            }
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            load(this);
        }
        
    }
    
}