package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.InvalidVehicleException;
import containing.Exceptions.NoFreeAgvException;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.StorageStrip.StorageState;
import containing.Road.Route;
import containing.Settings;
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
    
    public enum Side { LEFT, RIGHT }
    
    private final float WIDTH       = 400f;  // ???
    private final float LENGTH      = 1400f; // ???
    
    public final float STRIP_WIDTH  = 24f;   // ???
    public final float STRIP_LENGTH = WIDTH;
    
    private final int AGVS          = 100;
    private final float AGV_OFFSET  = 0f;
    
    private final StorageStrip[] strips;
    private Vector3f[] entrypoints;
    private Vector3f[] exitpoints;
    
    protected List<AGV> agvs;
    
    public StoragePlatform(Vector3f position)
    {
        super(position);
        strips = new StorageStrip[getStripAmount()];
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setEntrypoints();
        setExitpoints();
        setAxis(Platform.DynamicAxis.X);
        createStrips();
        createAgvSpots();
        createAllAgvs();
        /* no vehicles on this platform */
        extVehicleSpots = null;
        log("Created StoragePlatform object: " + toString());
    }
    
    public void loadContainerInAgv(Container container)
    {
        //todo
    }
    
    private void createAgvSpots()
    {
        float space = LENGTH / ((float)StorageStrip.MAX_AGV_SPOTS*getStripAmount() / 2f);
        float offset = (space / 2f) - ( AgvSpot.width / 2f);
        int subcount = 0;
        for(int i = 0; i < StorageStrip.MAX_AGV_SPOTS*getStripAmount(); i++) 
        {
            Vector3f agvSpotPosition;
            if(i % 2 == 0)
            {
                agvSpotPosition = new Vector3f(AGV_OFFSET, 0, space*subcount + offset);
            }
            else
            {
                agvSpotPosition = new Vector3f((WIDTH - AgvSpot.length) + AGV_OFFSET, 0, space*subcount + offset);
                subcount++;
            }
            agvSpots.add(new AgvSpot(agvSpotPosition));
        }
    }
    
    public final void createAllAgvs()
    {
        agvs = new ArrayList<>();
        
        for(int i = 0; i < AGVS; i++) 
            agvs.add(new AGV(this, new Vector3f(0,0,0)));
        
        int currentAgv = 0;
        for(int i = 1; i <= AGVS*2; i += 2)
        {
            try
            {
                agvSpots.get(i).ParkVehicle(agvs.get(currentAgv));
                agvs.get(currentAgv).setPosition(agvSpots.get(i).getPosition());
                currentAgv++;
            } 
            catch(InvalidVehicleException e) 
            {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
    public AGV requestFreeAgv(TransportType tt) throws NoFreeAgvException
    {
        List<Vector3f> waypoints = new ArrayList<>();
        Route route;
        switch(tt)
        {
            case Barge:
            case Seaship:
                for(int i = agvSpots.size() - 1; i >= 0; i--)
                {
                    AGV agv = (AGV)agvSpots.get(i).getParkedVehicle();
                    if(agv != null && agv.getIsAvailable())
                    {
                        //agv.followRoute(route);
                        return agv;
                    }
                }
                break;
            case Truck:
            case Train:
                for(int i = 0; i < agvSpots.size(); i++)
                {
                    AGV agv = (AGV)agvSpots.get(i).getParkedVehicle();
                    if(agv != null && agv.getIsAvailable())
                    {
                        //agv.followRoute(route);
                        return agv;
                    }
                }
                break;
        }
        throw new NoFreeAgvException("No free AGV available");
    }
    
    private void createStrips() 
    {
        Vector3f stripPosition;
        for(int i = 0; i < getStripAmount(); i++)
        {
            stripPosition = new Vector3f(0,0,STRIP_WIDTH*i);
            strips[i] = new StorageStrip(this, stripPosition);
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
    
    public int getStripAmount()
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
                    if(!strips[i].getStorageState().equals(StorageState.FULL))
                        return i;
                break;
            case Train:
            case Truck:
                for(int i = 0; i < getStripAmount(); i++)
                    if(!strips[i].getStorageState().equals(StorageState.FULL))
                        return i;
                break;
        }
        return 0;
    }
    
    public Vector3f getEntrypoint(Side side)
    {
        if(side.equals(Side.LEFT))
            return entrypoints[0];
        return entrypoints[1];
    }
    
    public Vector3f getExitpoint(Side side)
    {
        if(side.equals(Side.LEFT))
            return exitpoints[0];
        return exitpoints[1];
    }
    
    private void setEntrypoints()
    {
        entrypoints = new Vector3f[2];
        entrypoints[0] = new Vector3f(0,0,0);
        entrypoints[1] = new Vector3f(WIDTH, 0, 0);
        
    }
    
    private void setExitpoints()
    {
        exitpoints = new Vector3f[2];
        exitpoints[0] = new Vector3f(0,0,LENGTH);
        exitpoints[1] = new Vector3f(WIDTH, 0, LENGTH);
    }
    
    @Override
    public Vector3f getEntrypoint()
    {
        throw new UnsupportedOperationException("Pl0x add side as parameter (ex: getEntrypoint(Side.LEFT))");
    }
    
    @Override
    public Vector3f getExitpoint()
    {
        throw new UnsupportedOperationException("Pl0x add side as parameter (ex: getExitpoint(Side.LEFT))");
    }
    
    /* begin ignore */
    @Override
    protected void createCranes() { /*ignore */ }
    @Override
    protected void createExtVehicleSpots() {/* ignore */}
    /* end ignore */
    
    @Override
    public void update()
    {
        time += Settings.ClockDelay;
    }
    
}