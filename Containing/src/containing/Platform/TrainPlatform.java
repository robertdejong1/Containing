package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.AgvSpotOutOfBounds;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.NoFreeAgvException;
import containing.Exceptions.VehicleOverflowException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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
        setEntrypoint(new Vector3f(3.7f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + 1.5f));
        setExitpoint(new Vector3f(3.7f - (AGV.width*Settings.METER) / 2f, getPosition().y, getPosition().z + LENGTH));
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
            for(final ExternVehicle ev : extVehicles) {
                for(int i = 0; i < ev.getColumns().size(); i++) {
                    for(Container containerboi : ev.getGrid()[i][0]) {
                        if(containerboi != null)
                            System.out.println("row: " + i + " is een unloaded container");
                    }
                }
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
                    
                    for(final Crane c : cranes) {
                        System.out.println("currentCrane " + currentCrane + " == " + c.getIsAvailable());
                        if(c.getStatus() != Status.LOADING && c.getStatus() != Status.UNLOADING)
                            c.setIsAvailable(true);
                        if(currentCrane >= test && currentCrane < test + cranesPerVehicle && currentCrane*rowsPerCrane < unloadedColumns.size() && currentCrane <= agvQueue.size()-1) {
                            System.out.println("crane " + currentCrane + " == " + c.getStatus());
                            int startIndex = currentCrane * rowsPerCrane;
                            int colToGive = -1;
                            int rowUnloaded = 0;
                            Container containerToGive = null;
                            for(int i = startIndex; i < startIndex + rowsPerCrane; i++) 
                            {
                                boolean goFurther = false;
                                if(priorityColumns.contains(i) && !unloadedColumns.get(i)) {
                                    goFurther = true;
                                } else if(!unloadedColumns.get(i)) {
                                    goFurther = true;
                                }
                                if(goFurther) {
                                    List<Integer> unloadOrder = ev.getUnloadOrderY(i);
                                    Collections.reverse(unloadOrder);
                                    for(Integer row : unloadOrder) {
                                        for(Container container : ev.getGrid()[i][row]) {
                                            if(!goFurther)
                                                break;
                                            if(container != null) {
                                                colToGive = i;
                                                containerToGive = container;
                                                goFurther = false;
                                            } else {
                                                rowUnloaded++;
                                                if(rowUnloaded == unloadOrder.size()) {
                                                    unloadedColumns.set(i, true);
                                                    ev.updateColumn(i, true);
                                                }
                                            }
                                        }
                                        if(!goFurther)
                                                break;
                                    }
                                    if(!goFurther)
                                                break;
                                }
                            }
                            System.out.println("colToGive == " + colToGive + " < " + ev.getColumns().size());
                            if(!busyCranes.contains(c) && craneAgvs.get(currentCrane) == null && colToGive != -1) {
                                // move to right position of row
                                if(c.getStatus() == Status.WAITING) {
                                    System.out.println("ga nu weer kraan verplaatsen: " + colToGive);
                                    if(ev.getGrid()[colToGive][0][0] != null) {
                                        System.out.println("rowToGive: " + colToGive);
                                        c.followRoute(craneRoad.moveToContainer(ev, colToGive, c));
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
                                    agv.followRoute(road.getPathToParkingsSpot(agv, agvSpots.get(currentCrane)));
                                    craneAgvs.set(currentCrane, agv);
                                }
                                if(c.getStatus() == Status.WAITING && containerToGive != null) {
                                    try {
                                        c.load(containerToGive, ev);
                                    } catch (Exception ex) {
                                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            } else if(busyCranes.contains(c) && craneAgvs.get(currentCrane) != null && craneAgvs.get(currentCrane).getStatus() != Status.MOVING) {
                                if(c.getStatus() == Status.UNLOADING && craneAgvs.get(currentCrane).getStatus() != Status.MOVING) {
                                    try {
                                        AGV agv = craneAgvs.get(currentCrane);
                                        craneAgvs.set(currentCrane, null);
                                        c.unload(agv);
                                        busyCranes.remove(c);
                                    } catch (VehicleOverflowException ex) {
                                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ContainerNotFoundException ex) {
                                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (CargoOutOfBoundsException ex) {
                                        Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    System.out.println("deze agv staat er en er kan unload worden");
                                }
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