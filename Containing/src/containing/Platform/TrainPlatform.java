package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.AgvSpotOutOfBounds;
import containing.Exceptions.NoFreeAgvException;
import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.TrainSpot;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.TrainCrane;
import containing.Vehicle.Vehicle;
import containing.Vehicle.Vehicle.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TrainPlatform.java
 * The dynamic axis for cranes is: Z
 * @author Minardus
 */
public class TrainPlatform extends Platform {
    
    private final float WIDTH          = 103f*Settings.METER;  // ???
    private final float LENGTH         = 1523f*Settings.METER; // ???
    public final int MAX_VEHICLES      = 3;
    public final int CRANES            = 4;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 1.5f;   // ???
    private final float VEHICLE_OFFSET = 1.4f;
    
    private List<Vector3f> agvQueuePositions;
    
    private final Road craneRoad;
    
    public TrainPlatform(Vector3f position)
    {
        super(position, Platform.Positie.LINKS);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(DynamicAxis.Z);
        setEntrypoint(new Vector3f(3.7f, getPosition().y, getPosition().z + 1.5f));
        setExitpoint(new Vector3f(3.7f, getPosition().y, getPosition().z + LENGTH));
        setRoad();
        setTransportType(TransportType.Train);
        setMaxAgvQueue(CRANES*3);
        //createAgvSpots(new Vector3f(CRANE_OFFSET + TrainCrane.length + AGV_OFFSET, 0, 0));
        createExtVehicleSpots();
        createCranes();
        createAgvSpots();
        createAgvQueuePositions();
        for(int i = 0; i < cranes.size(); i++) {
            craneAgvs.add(null);
        }
        List waypoints = new ArrayList();
        waypoints.add(new Vector3f(2.6f, 5.5f, 0.5f));
        waypoints.add(new Vector3f(2.6f, 5.5f, 152.3f));
        craneRoad = new Road(waypoints);
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
        Collections.reverse(cranes);
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
    
    private void createAgvQueuePositions() {
        agvQueuePositions = new ArrayList<>();
        Vector3f base = new Vector3f(3.5f, 5.5f, 2f);
        for(int i = 0; i < maxAgvQueue; i++) {
            agvQueuePositions.add(new Vector3f(base.x, base.y, base.z + (AGV.length * Settings.METER) * i + 0.5f));
        }
        Collections.reverse(agvQueuePositions);
    }
    
    @Override
    public void unload() {
        /**
         * Als er een trein is geparkeerd:
         * 
         * 1 Vraag iets van 12 AGV's op en zet die op de road
         * 2 Zet de kranen op de juiste positie en ls er minimaal 1 AGV in de queue staat 
         *   (en de kraan waar hij heen gaat staat op de juiste positie) stuur een AGV naar de kraan
         * 4 Check telkens of er een AGV parkeerd staat bij de kraan, doe dan unload
         */
        
        super.unload();
        if(!extVehicles.isEmpty()) {
            int currentVehicle = 1;
            int cranesPerVehicle = cranes.size() / extVehicles.size();
            for(ExternVehicle ev : extVehicles) {
                // stap 1
                if(agvQueue.isEmpty() || agvQueue.size() < maxAgvQueue) {
                    for(int i = agvQueue.size(); i < (ev.getCargo().size() < maxAgvQueue ? ev.getCargo().size() : maxAgvQueue); i++) {
                        try {
                            AgvSpot agvSpot = Settings.port.getStoragePlatform().requestFreeAgv(getTransportType(), agvQueue);
                            AGV agv = (AGV)agvSpot.getParkedVehicle();
                            System.out.println("AGV haha: " + agv.getID());
                            // follow route, give end position from agvQueuePositions
                            //agv.followRoute(road.getPathAllIn(agv, agvSpot, Settings.port.getPlatforms().get(2).agvSpots.get(0), Settings.port.getPlatforms().get(2), Settings.port.getMainroad()));
                            agv.followRoute(road.getPathAllInVector(agv, agvSpot, agvQueuePositions.get(i), this, Settings.port.getMainroad()));
                            addAgvToQueue(agv);
                        } catch(NoFreeAgvException e) {
                            System.out.println("No Free AGV available ;(");
                        }
                        if(time >= 15) {
                            break;
                        }
                    }
                }
                // stap 2
                if(!agvQueue.isEmpty()) {
                    int test = (currentVehicle * cranesPerVehicle - cranesPerVehicle);
                    int rows = ev.getGridWidth();
                    int rowsPerCrane = rows / cranesPerVehicle;
                    List<Boolean> unloadedColumns = ev.getColumns();
                    List<Integer> priorityColumns = ev.getPriorityColumns();
                    int currentCrane = 0;
                    
                    for(Crane c : cranes) {
                        if(c.getIsAvailable() && currentCrane >= test && currentCrane < test + cranesPerVehicle && currentCrane*rowsPerCrane < unloadedColumns.size() && currentCrane <= agvQueue.size()-1) {
                            System.out.println("crane " + currentCrane + " == " + c.getStatus());
                            int startIndex = currentCrane * rowsPerCrane;
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
                            if(!busyCranes.contains(c) && craneAgvs.get(currentCrane) == null) {
                                // move to right position of row
                                if(c.getStatus() != Status.MOVING) {
                                    if(ev.getGrid()[rowToGive][0][0] != null) {
                                        System.out.println("rowToGive: " + rowToGive);
                                        c.followRoute(craneRoad.moveToContainer(ev, rowToGive, c));
                                        busyCranes.add(c);
                                    }
                                }
                            } else if(busyCranes.contains(c) && c.getStatus() != Status.MOVING && craneAgvs.get(currentCrane) == null) {
                                // adjust parkingspot
                                Vector3f cp = c.getPosition();
                                agvSpots.set(currentCrane, new AgvSpot(new Vector3f(cp.x + TrainCrane.length*Settings.METER, cp.y, cp.z)));
                                // send AGV from queue
                                AGV agv = agvQueue.peek();
                                if(agv.getStatus() != Status.MOVING) {
                                    agv = agvQueue.poll();
                                    System.out.println("ik ga AGV naar KRAAN STUREN ;(");
                                    System.out.println("de positie van dat kutding: " + agv.getPosition());
                                    System.out.println("waar ie heen wil: " + agvSpots.get(currentCrane).getPosition());
                                    System.out.println("gaat naar kraan: " + currentCrane);
                                    System.out.println("de positie van die kutkraan: " + c.getPosition());
                                    agv.followRoute(road.getPathToParkingsSpot(agv, agvSpots.get(currentCrane)));
                                    craneAgvs.set(currentCrane, agv);
                                }
                            } else if(busyCranes.contains(c) && craneAgvs.get(currentCrane) != null && craneAgvs.get(currentCrane).getStatus() != Status.MOVING) {
                                // unload
                                System.out.println("deze agv staat er en er kan unload worden");
                            }
                        }
                        currentCrane++;
                    }
                }
            }
        }
        time = 0;
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
            time++;
            if(time >= 15) {
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