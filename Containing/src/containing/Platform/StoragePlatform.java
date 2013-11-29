package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Platform.StorageStrip.StripState;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
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
    private StorageStrip[] strips;
    
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
        strips = new StorageStrip[getStripAmount()];
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
            strips[i] = new StorageStrip();
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
    
    private int getNearbyStrip(TransportType tt)
    {
        switch(tt)
        {
            case Barge:
            case Seaship:
                for(int i = getStripAmount() - 1; i >= 0; i--)
                    if(strips[i].getState().equals(StripState.FREE))
                        return i;
                break;
            case Train:
            case Truck:
                for(int i = 0; i < getStripAmount(); i++)
                    if(strips[i].getState().equals(StripState.FREE))
                        return i;
                break;
        }
        return 0;
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