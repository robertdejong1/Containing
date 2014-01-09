package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.Exceptions.AgvNotAvailable;
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
    
    public boolean hasContainer(Container container)
    {
        return containers.containsValue(container);
    }
    
    public Container unloadContainer(Point3D position)
    {
        return containers.remove(position);
    }
    
    // todo till, container kan eerder weg moeten, prioriteit is dan hoger
    private Point3D getFreeContainerPosition(Container container)
    {
        Date date = container.getDepartureDate();
        float from = container.getDepartureTimeFrom();
        int x, y, z;
        for(x = 0; x < MAX_X; x++)
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
                    } else {
                        return cur;
                    }
                }
            }
        }
        return null;
    }
    
    public StorageState getStorageState()
    {
        return storageState;
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
    
    public void checkParkedVehiclesLeft()
    {
        int startIndex = id * MAX_AGV_SPOTS;
        for(int i = startIndex; i < startIndex + MAX_AGV_SPOTS; i += 2)
        {
            AgvSpot agvSpot = platform.agvSpots.get(i);
            if(!agvSpot.isEmpty())
            {
                agvQueueLoad.add((AGV)agvSpot.getParkedVehicle());
                System.out.println("agv with id " + agvQueueLoad.peek().getID() + " is parked in agvspot " + i);
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
                platform.agvSpotQueue[i] = false;
            }
        }
    }
    
    private Vector3f getRealContainerPosition(Container container)
    {
        Point3D containerPosition = getFreeContainerPosition(container);
        float x = containerPosition.x*Container.depth;
        float y = containerPosition.y*Container.height;
        float z = containerPosition.z*Container.width;
        Vector3f realPosition = new Vector3f(x, y, z);
        return realPosition;
    }
    
    private Phase load_getPhase(AGV agv)
    {
        if(!craneBusy && crane.getStatus() == Status.WAITING && crane.getCargo().isEmpty())
        {
            return Phase.LOAD;
        }
        else if(craneBusy && agv.getCargo().isEmpty())
        {
            return Phase.SENDTOPARKINGSPOT;
        }
        return null;
    }
    
    /*
    private void load_phaseMoveToAgv(AGV agv)
    {
        Vector3f agvPosition = agv.getPosition();
        
        if(!agv.getCargo().isEmpty()) {
            crane.followRoute(craneRoad.moveToContainer(crane, new Vector3f(agvPosition.x, getPosition().y, getPosition().z)));
            craneBusy = true;
        }
    }
    */
    
    private void load_phaseLoad(AGV agv)
    {
        System.out.println("StorageStrip loading...");
        try {
            System.out.println("StorageStrip loading... IK BEN IN DE TRY BOIII");
            craneBusy = true;
            Vector3f pos = getRealContainerPosition(agv.getCargo().get(0));
            int psId = getParkingSpotIndexFromVehicleLoad(agv);
            while(psId > 12)
            {
                psId = psId - 12;
            }
            if(psId == 0) {}
            else if(psId < 12)
            {
                psId = psId / 2;
            }
            System.out.println("Load on StorageCrane: 'agv': " + agv.getID() + " 'pos': " + pos.toString() + " 'psId': " + psId);
            ((StorageCrane)crane).load(agv, pos, psId, getPosition());
        } catch (Exception ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    private void load_phaseUnload()
    {
        try {
            Container container = crane.unload();
            containers.put(getFreeContainerPosition(container), container);
        } catch (ContainerNotFoundException ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
    
    private void load_phaseSendToParkingSpot()
    {
        try {
            AGV agv = agvQueueLoad.peek();
            if(agv.getStatus() != Status.MOVING) {
                agvQueueLoad.poll().followRoute(platform.getLeft().getRoad().getPathAllIn(agv, getParkingSpotFromVehicleLoad(agv), platform.getFreeParkingSpotUnloaded(), platform, Settings.port.getMainroad()));
            }
        } catch (AgvNotAvailable ex) {
            Logger.getLogger(StorageStrip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
                    load_phaseLoad(craneAgv);
                    break;
                case SENDTOPARKINGSPOT:
                    System.out.println("SENDTOPARKINGSPOT");
                    //load_phaseSendToParkingSpot();
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
                //System.out.println("loading...");
                load();
            }
        }
        
        /* CHECK FOR PARKED VEHICLES */
        if(platform.time >= 10)
        {
            checkParkedVehiclesLeft();
            checkParkedVehiclesRight();
            platform.time = 0;
        }
        
        crane.update();
    }
    
}
