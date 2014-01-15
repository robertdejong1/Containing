package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.Exceptions.AgvNotAvailable;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.Platform.State;
import containing.Point3D;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.StorageCrane;
import containing.Vehicle.Vehicle.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StorageStrip.java
 * The dynamic axis is: X
 * @author Minardus
 */
public class StorageStrip implements Serializable {
    
    public enum StorageState { FREE, FULL }
    public enum StorageJobState { FREE, FULL }
    
    private enum Phase { LOAD, SENDTOPARKINGSPOT }
    
    private final int id;
    private State state = State.FREE;
    
    private StorageState storageState;
    
    public final static int MAX_AGV_SPOTS = 12;
    
    private final int MAX_X = 40;
    private final int MAX_Y = 6;
    private final int MAX_Z = 6;
    
    private final Vector3f position;
    private final Dimension2f dimension;
    
    private HashMap<Point3D, Container> containers;
    private PriorityQueue<Point3D> freePositions;
    
    private final List<AgvSpot> agvSpots;
    private Crane crane;
    private boolean craneBusy = false;
    
    private Queue<AGV> agvQueueLoad;
    private Queue<AGV> agvQueueUnload;
    
    private Road craneRoad;
    
    private boolean unloading = false;
    
    private final StoragePlatform platform;
    
    private int loadCounter = 0;
    
    /**
     * Create a strip on the StoragePlatform
     * @param platform the platform this strip is located
     * @param position the position on the storageplatform
     * @param id nr of strip
     */
    public StorageStrip(StoragePlatform platform, Vector3f position, int id) 
    {
        this.platform = platform;
        this.position = position;
        this.id = id;
        containers = new HashMap<>();
        freePositions = new PriorityQueue<>();
        storageState = StorageState.FREE;
        dimension = new Dimension2f(platform.STRIP_WIDTH, platform.STRIP_LENGTH);
        agvSpots = new ArrayList<>();
        agvQueueLoad = new LinkedList<>();
        agvQueueUnload = new LinkedList<>();
        setRoad();
        createCrane();
    }
    
    /**
     * Set the road for AGV's with 2 points
     */
    private void setRoad()
    {
        List<Vector3f> wayshit = new ArrayList<>();
        wayshit.add(new Vector3f(getPosition().x, getPosition().y, getPosition().z + (getDimension().width * id)));
        wayshit.add(new Vector3f(getPosition().x + getDimension().length, getPosition().y, getPosition().z + (getDimension().width * id)));
        craneRoad = new Road(wayshit);
    }
    
    /**
     * Return containers loaded on this strip
     * @return HashMap with containers and positions
     */
    public HashMap<Point3D, Container> getContainers() {
        return containers;
    }
    
    /**
     * Check if this strip has a specific container
     * @param container container which could be here
     * @return is container here
     */
    public boolean hasContainer(Container container)
    {
        return containers.containsValue(container);
    }
    
    /**
     * Remove container from strip
     * @param position position (index for HashMap)
     * @return index of container
     */
    public Container unloadContainer(Point3D position)
    {
        return containers.remove(position);
    }
    
    /**
     * Return free index for container
     * @param container the container to be loaded
     * @return position (index)
     */
    private Point3D getFreeContainerPosition(Container container)
    {
        Date date = container.getDepartureDate();
        float from = container.getDepartureTimeFrom();
        int x, y, z;
        for(x = 0; x < MAX_X; x++)
        {
            for(z = 0; z < MAX_Z; z++) 
            {
                for(y = 0; y < MAX_Y-1; y++) 
                {
                    Point3D cur = new Point3D(x,y,z);
                    System.out.println("container contains " + cur.toString() + " == " + containers.containsKey(cur));
                    if(containers.containsKey(cur) && y != (MAX_Y - 1))
                    {
                        Container c = containers.get(cur);
                        long curTimeStamp = Settings.getTimeStamp(c.getDepartureDate(), c.getDepartureTimeFrom());
                        System.out.println("container below: " + curTimeStamp);
                        long newTimeStamp = Settings.getTimeStamp(date, from);
                        System.out.println("new container: " + newTimeStamp);
                        if(newTimeStamp < curTimeStamp)
                        {
                            Point3D p = new Point3D(cur.x, cur.y+1, cur.z);
                            if(!containers.containsKey(p))
                                return p;
                        }
                    } else {
                        return cur;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Return the state of the Storage
     * @return the state (FULL, EMPTY)
     */
    public StorageState getStorageState()
    {
        return storageState;
    }
    
    /**
     * Create crane
     */
    private void createCrane()
    {
        Vector3f cranePosition = new Vector3f(getPosition().x,getPosition().y,getPosition().z);
        crane = new StorageCrane(cranePosition, platform);
        platform.cranes.add(crane);
    }
    
    /**
     * Return AGV parkingspots
     * @return list with parkingspots
     */
    public List<AgvSpot> getAgvSpots()
    {
        return agvSpots;
    }
    
    /**
     * Return position of this strip
     * @return position
     */
    public Vector3f getPosition()
    {
        return position;
    }
    
    /**
     * Return dimension of this strip
     * @return dimension
     */
    public Dimension2f getDimension()
    {
        return dimension;
    }
    
    /**
     * Return Free AGV Spot on the left (loading) side
     * @return parkingspot for AGV
     */
    public AgvSpot getFreeAgvSpotLoad()
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(agvSpot.isEmpty() && !platform.getAgvQueueLoadBusy(agvSpot)) {
                return agvSpot;
            }
        }
        return null;
    }
    
    /**
     * Return AGV parkingspot where AGV is parked (which is on the load side)
     * @param agv the agv
     * @return parking spot where the agv is parked
     */
    public AgvSpot getParkingSpotFromVehicleLoad(AGV agv)
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                if(agvSpot.getParkedVehicle().getID() == agv.getID())
                {
                    return agvSpot;
                }
            }
        }
        return null;
    }
    
    /**
     * Get the index of the parkingspot where the AGV is parked (on the loading side)
     * @param agv
     * @return index
     */
    public int getParkingSpotIndexFromVehicleLoad(AGV agv)
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                if(agvSpot.getParkedVehicle().getID() == agv.getID())
                {
                    return i;
                }
            }
        }
        return 0;
    }
    
    /**
     * Check if there are AGV's parked on the left side, then add
     * them to agvQueueLoad
     */
    public void checkParkedVehiclesLeft()
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                AGV agv = (AGV)agvSpot.getParkedVehicle();
                if(agv.getCargo().size() > 0 && !agvQueueLoad.contains(agv)) {
                    agvQueueLoad.add(agv);
                    System.out.println("added " + i + " to agvQueueLoad");
                }
            }
        }
    }
    
    /**
     * NOT USED: Check parked AGV's on the right side, if there is a AGV parked,
     * set them false in agvSpotQueue
     */
    public void checkParkedVehiclesRight()
    {
        int startIndex = id * MAX_AGV_SPOTS + 1;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                platform.agvSpotQueue[i] = false;
            }
        }
    }
    
    /**
     * Return the real (Vector3f) position of where the container is to be placed.
     * @param container the container
     * @return position
     */
    private Vector3f getRealContainerPosition(Container container)
    {
        Point3D containerPosition = getFreeContainerPosition(container);
        float x = containerPosition.x*Container.depth + position.x + 1.5f;
        float y = containerPosition.y*Container.height + position.y;
        float z = position.z;
        Vector3f realPosition = new Vector3f(x, y, z);
        return realPosition;
    }
    
    /**
     * LOAD PHASE : Return the actual phase of loading.
     * @param agv agv which brought a container
     * @return phase
     */
    private Phase load_getPhase(AGV agv)
    {
        if(!craneBusy && crane.getStatus() == Status.WAITING && crane.getCargo().isEmpty())
        {
            return Phase.LOAD;
        }
        else if(craneBusy && agv.getCargo().isEmpty() && crane.getStatus() == Status.WAITING)
        {
            return Phase.SENDTOPARKINGSPOT;
        }
        return null;
    }
    
    /**
     * LOAD PHASE : Load container onto crane and onto StorageStrip
     * @param agv the agv which brought a container
     */
    private void load_phaseLoad(AGV agv)
    {
        try {
            loadCounter++;
            craneBusy = true;
            Container cargo = agv.getCargo().get(0);
            Vector3f pos = getRealContainerPosition(cargo);
            System.out.println("realContainerPosition: " + pos.toString());
            Point3D containerIndex = getFreeContainerPosition(cargo);
            System.out.println("containerPosition: " + containerIndex.toString());
            containers.put(containerIndex, cargo);
            int psId = getParkingSpotIndexFromVehicleLoad(agv);
            while(psId >= 12)
            {
                psId = psId - 12;
            }
            if(psId == 0) {}
            else if(psId < 12)
            {
                psId = psId / 2;
            }
            System.out.println("Load on StorageCrane: 'agv': " + agv.getID() + " 'pos': " + pos.toString() + " 'psId': " + psId);
            ((StorageCrane)crane).load(agv, pos, psId, getPosition(), containerIndex);
        } catch (Exception ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * LOAD PHASE : Send unloaded AGV to parkingspot
     */
    private void load_phaseSendToParkingSpot()
    {
        try {
            AGV agv = agvQueueLoad.peek();
            if(agv.getStatus() != Status.MOVING) {
                agv = agvQueueLoad.poll();
                AgvSpot agvSpot = getParkingSpotFromVehicleLoad(agv);
                platform.removeAgvQueueLoadBusy(agvSpot);
                agv.followRoute(platform.getLeft().getRoad().getPathAllIn(agv, getParkingSpotFromVehicleLoad(agv), platform.getFreeParkingSpotUnloaded(), platform, Settings.port.getMainroad()));
                craneBusy = false;
            }
        } catch (AgvNotAvailable ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * LOAD PHASE : The call (control) function (is called in update)
     */
    public void load()
    {
        AGV craneAgv = agvQueueLoad.peek();
        Phase phase = load_getPhase(craneAgv);
        if(phase != null)
        {
            switch(phase)
            {
                case LOAD:
                    System.out.println("LOAD");
                    if(!craneAgv.getCargo().isEmpty())
                        load_phaseLoad(craneAgv);
                    break;
                case SENDTOPARKINGSPOT:
                    System.out.println("SENDTOPARKINGSPOT");
                    load_phaseSendToParkingSpot();
                    break;
            }
        }
        System.out.println("Crane cargo == " + crane.getCargo().size());
    }
    
    /**
     * Called every 100ms
     */
    public void update() {
        if(state.equals(State.FREE)) {}
        
        if(false) {
            state = State.UNLOAD;
        } else if(!agvQueueLoad.isEmpty()) {
            state = State.LOAD;
        } else {
            state = State.FREE;
        }
        
        /* UNLOAD EXTERNAL VEHICLE */
        if(state.equals(State.UNLOAD))
        {
            
        }
        
        /* LOAD EXTERNAL VEHICLE */
        if(state.equals(State.LOAD))
        {
            if(platform.time >= 10) {
                //System.out.println("loading... " + id);
                load();
            }
        }
        
        /* CHECK FOR PARKED VEHICLES */
        if(platform.time >= 10)
        {
            checkParkedVehiclesLeft();
            checkParkedVehiclesRight();
            if(id == platform.getStripAmount()-1)
                platform.time = 0;
        }
        
        crane.update();
    }
    
}
