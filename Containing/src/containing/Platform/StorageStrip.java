package containing.Platform;

import containing.Container;
import containing.ErrorLog;
import containing.Exceptions.StorageOverflowException;
import containing.Point3D;
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
    public enum JobState { FREE, FULL }
    
    private StorageState state = StorageState.FULL;
    
    private final int MAX_X = 40;
    private final int MAX_Y = 6;
    private final int MAX_Z = 6;
    
    private Container[][][] containers;
    private Queue<StorageJob> jobs;
    
    public StorageStrip() 
    {
        containers = new Container[MAX_X][MAX_Y][MAX_Z];
        state = StorageState.FREE;
    }
    
    public void createJob(int stripId, Container c)
    {
        jobs.add(new StorageJob(stripId, c));
    }
    
    public StorageJob getJob()
    {
        return jobs.remove();
    }
    
    public boolean hasContainer(Container container)
    {
        return true; //todo
    }
    
    public void addContainer(Container c) throws StorageOverflowException
    {
        Point3D pos = getFreeContainerPosition(c.getDepartureDate(), c.getDepartureTimeFrom(), c.getDepartureTimeTill());
        if(pos == null)
            throw new StorageOverflowException("");
    }
    
    private Point3D getFreeContainerPosition(Date date, float from, float till)
    {
        if(true)
            return new Point3D(0,0,0);
        return null;
    }
    
    public StorageState getState()
    {
        return state;
    }
    
}
