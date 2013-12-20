package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.Exceptions.ContainerNotFoundException;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.Platform.State;
import containing.Platform.StoragePlatform.Side;
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
    
    private enum Phase { MOVETOAGV, LOAD, MOVETOFREEPOSITION, UNLOAD, SENDTOPARKINGSPOT }
    
    private final int id;
    private State state = State.FREE;
    
    private StorageState storageState;
    private StorageJobState storageJobState;
    
    public final static int MAX_AGV_SPOTS = 12;
    
    private final int MAX_X = 40;
    private final int MAX_Y = 6;
    private final int MAX_Z = 6;
    
    private final int MAX_JOBS = 12;
    
    private final Vector3f position;
    private final Dimension2f dimension;
    
    private HashMap<Point3D, Container> containers;
    private PriorityQueue<Point3D> freePositions;
    private Queue<StorageJob> jobs;
    
    private final List<AgvSpot> agvSpots;
    private Crane crane;
    private boolean craneBusy = false;
    
    private Queue<AGV> agvQueueLoad;
    private Queue<AGV> agvQueueUnload;
    
    private Road craneRoad;
    
    private final StoragePlatform platform;
    
    public StorageStrip(StoragePlatform platform, Vector3f position, int id) 
    {
        this.platform = platform;
        this.position = position;
        this.id = id;
        containers = new HashMap<>();
        freePositions = new PriorityQueue<>();
        storageState = StorageState.FREE;
        storageJobState = StorageJobState.FREE;
        dimension = new Dimension2f(platform.STRIP_WIDTH, platform.STRIP_LENGTH);
        agvSpots = new ArrayList<>();
        agvQueueLoad = new LinkedList<>();
        agvQueueUnload = new LinkedList<>();
        setRoad();
        createCrane();
    }
    
    private void setRoad()
    {
        List<Vector3f> wayshit = new ArrayList<>();
        wayshit.add(new Vector3f(getPosition().x, getPosition().y, getPosition().z + (getDimension().width * id)));
        wayshit.add(new Vector3f(getPosition().x + getDimension().length, getPosition().y, getPosition().z + (getDimension().width * id)));
        craneRoad = new Road(wayshit);
    }
    
    public HashMap<Point3D, Container> getContainers() {
        return containers;
    }
    
    public void createJob(int stripId, Container c)
    {
        if(jobs.size() < MAX_JOBS)
        {
            Point3D pos = getFreeContainerPosition(c.getDepartureDate(), c.getDepartureTimeFrom(), c.getDepartureTimeTill(), Side.LEFT);
            jobs.add(new StorageJob(stripId, pos, c));
            if(jobs.size() == MAX_JOBS)
                storageJobState = StorageJobState.FULL;
        }
    }
    
    public StorageJob getJob()
    {
        if(jobs.size() == MAX_JOBS)
            storageJobState = StorageJobState.FREE;
        return jobs.poll();
    }
    
    public boolean hasContainer(Container container)
    {
        return containers.containsValue(container);
    }
    
    public void loadContainer(StorageJob job)
    {
        // geef job aan kraan met container en positie
    }
    
    public Container unloadContainer(Point3D position)
    {
        return containers.remove(position);
    }
    
    // todo till, container kan eerder weg moeten, prioriteit is dan hoger
    private Point3D getFreeContainerPosition(Date date, float from, float till, Side side)
    {
        int x, y, z;
        for(x = 0; (side.equals(Side.LEFT) ? x < MAX_X : MAX_X > x);)
        {
            for(y = 0; y < MAX_Y; y++) 
            {
                for(z = 0; z < MAX_Z; z++) 
                {
                    Point3D cur = new Point3D(x,y,z);
                    if(containers.containsKey(cur) && y != (MAX_Y - 1))
                    {
                        Container c = containers.get(cur);
                        long curTimeStamp = Settings.getTimeStamp(c.getDepartureDate(), c.getDepartureTimeFrom());
                        long newTimeStamp = Settings.getTimeStamp(date, from);
                        if(newTimeStamp < curTimeStamp)
                        {
                            return cur;
                        }
                    }
                }
            }
            x = (side.equals(Side.LEFT) ? ++x : --x);
        }
        return null;
    }
    
    public StorageState getStorageState()
    {
        return storageState;
    }
    
    public StorageJobState getStorageJobState()
    {
        return storageJobState;
    }
    
    private void createCrane()
    {
        Vector3f cranePosition = new Vector3f(getPosition().x,getPosition().y,getPosition().z);
        crane = new StorageCrane(cranePosition, platform);
        platform.cranes.add(crane);
    }
    
    public List<AgvSpot> getAgvSpots()
    {
        return agvSpots;
    }
    
    public Vector3f getPosition()
    {
        return position;
    }
    
    public Dimension2f getDimension()
    {
        return dimension;
    }
    
    public void checkParkedVehiclesLeft()
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                agvQueueLoad.add((AGV)agvSpot.getParkedVehicle());
            }
        }
    }
    
    public void checkParkedVehiclesRight()
    {
        int startIndex = id * MAX_AGV_SPOTS + 1;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                agvQueueUnload.add((AGV)agvSpot.getParkedVehicle());
            }
        }
    }
    
    private Phase getPhase()
    {
        if(!craneBusy && crane.getStatus() != Status.MOVING)
        {
            return Phase.MOVETOAGV;
        }
        return null;
    }
    
    private void phaseMoveToAgv(AGV agv)
    {
        if(!agv.getCargo().isEmpty()) {
            //crane.followRoute(craneRoad.);
            craneBusy = true;
        }
    }
    
    private void phaseLoad(AGV agv)
    {
        try {
            crane.load(agv.unload());
        } catch (Exception ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void phaseMoveToFreePosition()
    {
        
    }
    
    public void load()
    {
        AGV craneAgv = agvQueueLoad.peek();
        Phase phase = getPhase();
        if(phase != null)
        {
            switch(phase)
            {
                case MOVETOAGV:
                    System.out.println("MOVETOAGV");
                    phaseMoveToAgv(craneAgv);
                    break;
                case LOAD:
                    System.out.println("LOAD");
                    phaseLoad(craneAgv);
                    break;
                case MOVETOFREEPOSITION:
                    System.out.println("MOVETOFREEPOSITION");
                    phaseMoveToFreePosition();
                    break;
                case UNLOAD:
                    System.out.println("UNLOAD");
                    //phaseUnload();
                    break;
                case SENDTOPARKINGSPOT:
                    System.out.println("SENDTOPARKINGSPOT");
                    //phaseSendToParkingSpot();
                    break;
            }
        }
    }
    
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
                System.out.println("loading...");
                load();
            }
        }
        
        /* CHECK FOR PARKED VEHICLES */
        if(platform.time >= 10)
        {
            checkParkedVehiclesLeft();
            platform.time = 0;
        }
    }
    
}
