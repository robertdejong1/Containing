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
        setEntrypoint(new Vector3f(717f*Settings.METER, getPosition().y, getPosition().z));
        setExitpoint(new Vector3f(717f*Settings.METER, getPosition().y, getPosition().z + LENGTH));
        setEntrycorner(new Vector3f(getPosition().x + WIDTH, getPosition().y, getPosition().z));
        setExitcorner(new Vector3f(getPosition().x + WIDTH, getPosition().y, getPosition().z + LENGTH));
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
        super.unload();
        if(!extVehicles.isEmpty()) {
            int currentVehicle = 1;
            int cranesPerVehicle = cranes.size() / extVehicles.size();
            System.out.println("cranes per vehicle: " + cranesPerVehicle);
            for(ExternVehicle ev : extVehicles) {
                int test = (currentVehicle * cranesPerVehicle - cranesPerVehicle);
                
                int rows = ev.getGridWidth();
                int rowsPerCrane = rows / cranesPerVehicle;

                List<Boolean> unloadedColumns = ev.getColumns();
                List<Integer> priorityColumns = ev.getPriorityColumns();

                int currentCrane = 0;
                for(Crane c : cranes)
                {
                    System.out.println("test: " + test);
                    if(!busyCranes.contains(c) && c.getIsAvailable() && currentCrane >= test && currentCrane < test + cranesPerVehicle) {
                        System.out.println("kom ik hier???");
                        busyCranes.add(c);
                        int startIndex = currentCrane + rowsPerCrane;
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
                        final AGV agv = (AGV)agvSpot.getParkedVehicle();
                        
                        System.out.println("ik ga nu die motherfucking agv een route geven bitch!");
                        agv.followRoute(agv.getCurrentPlatform().road.getPathAllIn(agv, agvSpot, Settings.port.getPlatforms().get(2).agvSpots.get(0), Settings.port.getPlatforms().get(2)));
                        //agv.followRoute(Settings.port.getStoragePlatform().road.getPath(agv, agvSpot, Settings.port.getStoragePlatform().getExitpoint()));
                        //agv.followRoute(Settings.port.getMainroad().getPath(agv, Settings.port.getStoragePlatform().getExitpoint(), Settings.port.getPlatforms().get(2)));
                        //agv.followRoute(road.getPath(agv, Settings.port.getPlatforms().get(2).agvSpots.get(_craneId)));
                        break;
                    }
                    currentCrane++;
                    break;
                }
                currentVehicle++;
                break;
            }
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