package containing.Platform;

import containing.Container;
import containing.ErrorLog;
import containing.Exceptions.StorageOverflowException;
import containing.Point3D;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Queue;

/**
 * StorageStrip.java
 * Keeps track of positions of containers
 * @author Minardus
 */
public class StorageStrip {
    
    public enum StorageState { FREE, FULL }
    public enum StorageJobState { FREE, FULL }
    private enum Side { LEFT, RIGHT }
    
    private StorageState storageState;
    private StorageJobState storageJobState;
    
    private final int MAX_X = 40;
    private final int MAX_Y = 6;
    private final int MAX_Z = 6;
    
    private final int MAX_JOBS = 12;
    
    private HashMap<Point3D, Container> containers;
    private Queue<StorageJob> jobs;
    
    public StorageStrip() 
    {
        containers = new HashMap<>();
        storageState = StorageState.FREE;
        storageJobState = StorageJobState.FREE;
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
    
}
