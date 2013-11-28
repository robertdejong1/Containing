package containing.Platform;

import containing.Container;
import containing.Dimension2f;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * SeashipPlatform.java
 * The dynamic axis is: X
 * @author Minardus
 */
public class StoragePlatform extends Platform {
    
    private final float WIDTH          = 400f;  // ???
    private final float LENGTH         = 1400f; // ???
    private final int CRANES           = 10;
    
    private final float AGV_OFFSET     = 0f;
    private final float CRANE_OFFSET   = 0f;    // ???
    private final float STRIP_WIDTH    = 24f;   // ???
    private final int AGVS             = 100;
    
    //private HashMap<Integer, StorageStrip> strips;
    private List<StorageStrip> strips;
    
    public StoragePlatform(Vector3f position)
    {
        super(position);
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setAxis(Platform.DynamicAxis.X);
        createStrips();
        createAgvSpots(new Vector3f(0, 0, 0));  //todo
        createCranes();
        /* no vehicles on this platform */
        extVehicleSpots = null;
        /* initialize arraylists */
        strips = new ArrayList<>();
        log("Created StoragePlatform object: " + toString());
    }
    
    public List<AGV> getAllCreatedAgvs()
    {
        List<AGV> agvs = new ArrayList<>();
        for(int i = 0; i < AGVS; i++)
            agvs.add(new AGV(this, new Vector3f(0,0,0), this)); // todo
        return agvs;
    }
    
    private void createStrips() 
    {
        for(int i = 0; i < getStripAmount(); i++)
        {
            strips.add(new StorageStrip());
        }
    }
    
    public boolean hasContainer(Container container)
    {
        for(StorageStrip strip : strips)
        {
            if(strip.hasContainer(container))
                return true;
        }
        return false;
    }
    
    private int getStripAmount()
    {
        return (int)((float)LENGTH / STRIP_WIDTH);
    }
    
    @Override
    protected final void createCranes() 
    {
        //todo
    }

    @Override
    protected void createExtVehicleSpots() {/* ignore */}
    
    @Override
    public void update()
    {
        //todo
    }
    
}