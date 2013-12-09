package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.StoragePlatform.Side;
import containing.Point3D;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.Crane;
import containing.Vehicle.StorageCrane;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

/**
 * StorageStrip.java
 * The dynamic axis is: X
 * @author Minardus
 */
public class StorageStrip implements Serializable {
    
    public enum StorageState { FREE, FULL }
    public enum StorageJobState { FREE, FULL }
    
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
    private Queue<StorageJob> jobs;
    
    private final List<AgvSpot> agvSpots;
    private Crane crane;
    
    private final StoragePlatform platform;
    
    public StorageStrip(StoragePlatform platform, Vector3f position) 
    {
        this.platform = platform;
        this.position = position;
        containers = new HashMap<>();
        storageState = StorageState.FREE;
        storageJobState = StorageJobState.FREE;
        dimension = new Dimension2f(platform.STRIP_WIDTH, platform.STRIP_LENGTH);
        agvSpots = new ArrayList<>();
        createCrane();
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
        Vector3f cranePosition = new Vector3f(0,0,0);
        crane = new StorageCrane(cranePosition, platform);
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
    
}
