package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.AgvSpotOutOfBounds;
import containing.Exceptions.NoFreeAgvException;
import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.TrainSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Train;
import containing.Vehicle.TrainCrane;
import containing.Vehicle.Vehicle;
import java.util.List;

/**
 * TrainPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TrainPlatform extends Platform {
    
    private boolean doItOnce = true;
    
    private final float WIDTH          = 103f*Settings.METER;  // ???
    private final float LENGTH         = 1523f*Settings.METER; // ???
    public final int MAX_VEHICLES      = 3;
    public final int CRANES            = 4;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 1.5f;   // ???
    private final float VEHICLE_OFFSET = 1.4f;
    
    public TrainPlatform(Vector3f position)
    {
        super(position);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(getPosition().x + WIDTH, getPosition().y, getPosition().z));
        setExitpoint(new Vector3f(getPosition().x + WIDTH, getPosition().y, getPosition().z + LENGTH));
        setRoad();
        setTransportType(TransportType.Train);
        setMaxAgvQueue(CRANES);
        //createAgvSpots(new Vector3f(CRANE_OFFSET + TrainCrane.length + AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        createAgvSpots();
        log("Created TrainPlatform object: " + toString());
    }
    
    protected final void createAgvSpots() {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( TrainCrane.width*Settings.METER / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(getPosition().x + CRANE_OFFSET, getPosition().y, getPosition().z + (space*i + offset));
            float x = cranePosition.x;
            float y = cranePosition.y;
            float z = cranePosition.z;
            agvSpots.add(new AgvSpot(new Vector3f(x + TrainCrane.length*Settings.METER, y, z)));
        }
    }

    @Override
    protected final void createCranes() 
    {
        float space = LENGTH / (float)CRANES;
        float offset = (space / 2f) - ( TrainCrane.width*Settings.METER / 2f);
        for(int i = 0; i < CRANES; i++) 
        {
            Vector3f cranePosition = new Vector3f(getPosition().x + CRANE_OFFSET, getPosition().y, getPosition().z + (space*i + offset));
            cranes.add(new TrainCrane(cranePosition, this));
        }
    }
    
    @Override
    protected final void createExtVehicleSpots() 
    {
        float space = LENGTH / (float)MAX_VEHICLES;
        //float offset = (space / 2f) - (TrainSpot.length*Settings.METER / 2f);
        for(int i = 0; i < MAX_VEHICLES; i++)
        {
            Vector3f spotPosition = new Vector3f(getPosition().x + VEHICLE_OFFSET, getPosition().y, (getPosition().z + LENGTH) - space*i);
            extVehicleSpots.add(new TrainSpot(spotPosition));
        }
    }
    
    @Override
    public void unload() {
        evs.clear();
        for(int i = 0; i < extVehicleSpots.size(); i++) 
        {
            if(extVehicleSpots.get(i).getParkedVehicle() != null) {
                evs.add((Train)extVehicleSpots.get(i).getParkedVehicle());
            }
        }
        
        int rows = evs.get(0).getGridWidth();
        int rowsPerCrane = rows / cranes.size();
        
        List<Boolean> unloadedColumns = evs.get(0).getColumns();
        List<Integer> priorityColumns = evs.get(0).getPriorityColumns();
        
        int craneId = 0;
        for(Crane c : cranes)
        {
            if(!busyCranes.contains(c) && c.getIsAvailable()) {
                int startIndex = craneId + rowsPerCrane;
                int rowToGive = 0;
                for(int i = startIndex; i < startIndex + rowsPerCrane; i++) 
                {
                    if(priorityColumns.contains(i) && !unloadedColumns.get(i)) {
                        rowToGive = i;
                        break;
                    } else if(!unloadedColumns.get(i)) {
                        rowToGive = i;
                        break;
                    }
                }
                
                int spot = 0;
                try {
                    spot = Settings.port.getStoragePlatform().requestFreeAgv(getTransportType());
                } catch(NoFreeAgvException e) {/*ignore*/}
                
                final AgvSpot agvSpot = (AgvSpot)Settings.port.getStoragePlatform().agvSpots.get(spot);
                
                final int _craneId = craneId;
                final AGV agv = (AGV)agvSpot.getParkedVehicle();
                new Thread() {
                    
                    @Override
                    public void run() {
                        agv.followRoute(Settings.port.getStoragePlatform().road.getPath(agv, agvSpot, Settings.port.getStoragePlatform().getExitpoint()));
                        while(agv.getStatus() == Vehicle.Status.MOVING) {
                            try {
                                Thread.sleep(Settings.ClockDelay);
                                //System.out.println("Route 1: agv == MOVING");
                            } catch(InterruptedException e) {/*ignore*/}
                        }
                        agv.followRoute(Settings.port.getMainroad().getPath(agv, Settings.port.getStoragePlatform().getExitpoint(), Settings.port.getPlatforms().get(2)));
                        while(agv.getStatus() == Vehicle.Status.MOVING) {
                            try {
                                Thread.sleep(Settings.ClockDelay);
                                agv.update();
                                //System.out.println("Route 2: agv == MOVING");
                            } catch(InterruptedException e) {/*ignore*/}
                        }
                        agv.followRoute(road.getPath(agv, Settings.port.getPlatforms().get(2).agvSpots.get(_craneId)));
                        while(agv.getStatus() == Vehicle.Status.MOVING) {
                            try {
                                Thread.sleep(Settings.ClockDelay);
                                agv.update();
                                //System.out.println("Route 3: agv == MOVING");
                            } catch(InterruptedException e) {/*ignore*/}
                        }
                    }
                    
                }.start();
                
            }
            craneId++;
        }
    }
    
    @Override
    public void update()
    {
        super.update();
        
        /* if platform is free, request next job */
        if(state.equals(State.FREE))
            requestNextJob();
        
        if(hasExtVehicle()) {
            state = State.UNLOAD;
        } else if(jobs.size() > 0) {
            state = State.LOAD;
        } else {
            state = State.FREE;
        }
        
        /* UNLOAD EXTERNAL VEHICLE */
        if(state.equals(State.UNLOAD))
        {
            if(doItOnce) {
                unload();
                doItOnce = false;
            }
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            load(this);
        }
    }
    
}