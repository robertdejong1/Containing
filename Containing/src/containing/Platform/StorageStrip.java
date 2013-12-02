package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.StoragePlatform.Side;
import containing.Point3D;
import containing.Vector3f;
import containing.Vehicle.Crane;
import containing.Vehicle.StorageCrane;
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
public class StorageStrip {
    
    public enum StorageState { FREE, FULL }
    public enum StorageJobState { FREE, FULL }
    
    private StorageState storageState;
    private StorageJobState storageJobState;
    
    private final float AGV_OFFSET  = 0f;
    private final int MAX_AGV_SPOTS = 12;
    
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
        createAgvSpots();
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
                        long curTimeStamp = getTimeStamp(c.getDepartureDate(), c.getDepartureTimeFrom());
                        long newTimeStamp = getTimeStamp(date, from);
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
    
    private long getTimeStamp(Date date, float from)
    {
        return date.getTime() + (long)((int)(from*3600) * 1000) + (long)((((from % 1) * 100) * 60) * 1000);
    }
    
    public StorageState getStorageState()
    {
        return storageState;
    }
    
    public StorageJobState getStorageJobState()
    {
        return storageJobState;
    }
    
    private void createAgvSpots()
    {
        float space = dimension.width / ((float)MAX_AGV_SPOTS / 2f);
        float offset = (space / 2f) - ( AgvSpot.width / 2f);
        int subcount = 0;
        for(int i = 0; i < MAX_AGV_SPOTS*2; i++) 
        {
            Vector3f agvSpotPosition;
            if(i % 2 == 0)
            {
                agvSpotPosition = new Vector3f(AGV_OFFSET, 0, space*subcount + offset);
            }
            else
            {
                agvSpotPosition = new Vector3f((dimension.length - AgvSpot.length) + AGV_OFFSET, 0, space*subcount + offset);
                subcount++;
            }
            agvSpots.add(new AgvSpot(agvSpotPosition));
        }
    }
    
    private void createCrane()
    {
        Vector3f cranePosition = new Vector3f(0,0,0);
        crane = new StorageCrane(cranePosition, platform);
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
