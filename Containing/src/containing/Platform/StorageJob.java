package containing.Platform;

import containing.Container;
import containing.Point3D;

public class StorageJob {
    
    public enum StorageJobType { LOAD, UNLOAD }
    
    private int stripId;
    private Point3D position;
    private Container container;
    
    public StorageJob(int stripId, Point3D position, Container container)
    {
        this.stripId = stripId;
        this.position = position;
        this.container = container;
    }
    
}
