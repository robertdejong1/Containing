package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Exceptions.InvalidVehicleException;
import containing.Exceptions.NoFreeAgvException;
import containing.ParkingSpot.AgvSpot;
import containing.Platform.StorageStrip.StorageState;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * SeashipPlatform.java
 * The dynamic axis is: X
 * @author Minardus
 */
public class StoragePlatform extends Platform {
    
    public enum Side { LEFT, RIGHT }
    
    private final float WIDTH       = 600f*Settings.METER;  // ???
    private final float LENGTH      = 1525f*Settings.METER; // ???
    
    public final float STRIP_WIDTH  = 25f*Settings.METER;   // ???
    public final float STRIP_LENGTH = WIDTH;
    
    private final int AGVS          = 100;
    private final float AGV_OFFSET  = 1.9f;
    private final float ROAD_OFFSET = 1.65f;
    
    private final StorageStrip[] strips;
    private Vector3f[] entrypoints;
    private Vector3f[] entrycorners;
    private Vector3f[] exitcorners;
    private Vector3f[] exitpoints;
    
    protected List<AGV> agvs;
    
    protected List<StoragePlatformOrientation> orientation;
    
    protected Road road2;
    
    protected boolean[] agvSpotQueue;
    
    public StoragePlatform(Vector3f position)
    {
        super(position, Positie.RECHTS);
        strips = new StorageStrip[getStripAmount()];
        setDimension(new Dimension2f(WIDTH, LENGTH));
        setEntrypoints();
        setEntrycorners();
        setExitcorners();
        setExitpoints();
        setAxis(Platform.DynamicAxis.X);
        createStrips();
        createAgvSpots();
        createAllAgvs();
        setRoad();
        agvSpotQueue = new boolean[agvSpots.size() / 2];
        Arrays.fill(agvSpotQueue, false);
        /* no vehicles on this platform */
        extVehicleSpots = null;
        log("Created StoragePlatform object: " + toString());
    }
    
    public StorageStrip[] getStrips() 
    {
        return strips;
    }
    
    public StorageStrip getStrip(int nr)
    {
        return strips[nr];
    }
    
    public AgvSpot getFreeParkingSpotUnloaded()
    {
        for(int i = 1; i < agvSpots.size(); i += 2)
        {
            if(agvSpotQueue[i] == false) {
                agvSpotQueue[i] = true;
                return agvSpots.get(i);
            }
        }
        return agvSpots.get(0);
    }
    
    @Override
    protected final void setRoad() 
    {
        Vector3f[] corners = new Vector3f[2];
        orientation = new ArrayList<>();
        List<Vector3f> wayshit = new ArrayList<>();
        wayshit.add(getEntrypoint(Side.RIGHT));
        //wayshit.add(getEntrycorner(Side.RIGHT));
        //wayshit.add(getExitcorner(Side.RIGHT));
        wayshit.add(getExitpoint(Side.RIGHT));
        corners[0] = getEntrycorner(Side.RIGHT);
        corners[1] = getEntrycorner(Side.RIGHT);
        orientation.add(new StoragePlatformOrientation(positie.RECHTS, new Road(wayshit), getEntrypoint(Side.RIGHT), getExitpoint(Side.RIGHT), corners));
        road = new Road(wayshit);
        wayshit.clear();
        wayshit.add(getEntrypoint(Side.LEFT));
        //wayshit.add(getEntrycorner(Side.LEFT));
        //wayshit.add(getExitcorner(Side.LEFT));
        wayshit.add(getExitpoint(Side.LEFT));
        corners[0] = getEntrycorner(Side.LEFT);
        corners[1] = getEntrycorner(Side.LEFT);
        orientation.add(new StoragePlatformOrientation(positie.LINKS, new Road(wayshit), getEntrypoint(Side.LEFT), getExitpoint(Side.LEFT), corners));
        road2 = new Road(wayshit);
    }
    
    public void loadContainerInAgv(Container container, Platform platformToGo)
    {
        //todo
    }
    
    private void createAgvSpots()
    {
        int subcount = 0;
        float offset = 1f*Settings.METER;
        float z = getPosition().z;
        for(int i = 0; i < StorageStrip.MAX_AGV_SPOTS*getStripAmount(); i++) 
        {
            if(i % 12 == 0 && i != 0) {
                z += 1f*Settings.METER;
            }
            Vector3f agvSpotPosition;
            if(i % 2 == 0)
            {
                agvSpotPosition = new Vector3f(getPosition().x + AGV_OFFSET  - (AGV.length*Settings.METER)*4 + 0.25f, getPosition().y, z + (subcount*offset) + (AGV.width*subcount)*Settings.METER + 0.05f*subcount + 0.15f + (AGV.width*Settings.METER) / 2f);
            }
            else
            {
                agvSpotPosition = new Vector3f(getPosition().x + WIDTH - AGV_OFFSET - (AGV.length*Settings.METER)*4 + 0.25f, getPosition().y, z + (subcount*offset) + (AGV.width*subcount)*Settings.METER + 0.05f*subcount + 0.15f + (AGV.width*Settings.METER) / 2f);
                subcount++;
            }
            agvSpots.add(new AgvSpot(agvSpotPosition));
        }
    }
    
    public final void createAllAgvs()
    {
        agvs = new ArrayList<>();
        
        for(int i = 0; i < AGVS; i++) { 
            agvs.add(new AGV(this, new Vector3f(0,0,0)));
        }
        
        int currentAgv = 0;
        for(int i = 1; i <= AGVS*2; i += 2)
        {
            try
            {
                agvs.get(currentAgv).setPosition(agvSpots.get(i).getPosition());
                agvs.get(currentAgv).setCurrentPlatform(this);
                agvSpots.get(i).ParkVehicle(agvs.get(currentAgv));
                currentAgv++;
            } 
            catch(InvalidVehicleException e) 
            {
                System.out.println(e.getMessage());
            }
        }
        
    }
    
    public List<AGV> getAgvs() {
        return agvs;
    }
    
    public AgvSpot requestFreeAgv(TransportType tt, Queue<AGV> agvQueue) throws NoFreeAgvException
    {
        switch(tt)
        {
            case Barge:
            case Seaship:
                for(int i = agvSpots.size() - 1; i >= 0; i--)
                {
                    AGV agv = (AGV)agvSpots.get(i).getParkedVehicle();
                    if(agv != null && agv.getIsAvailable() && !agvQueue.contains(agv))
                    {
                        //agv.followRoute(route);
                        return agvSpots.get(i);
                    }
                }
                break;
            case Truck:
            case Train:
                for(int i = 0; i < agvSpots.size(); i++)
                {
                    AGV agv = (AGV)agvSpots.get(i).getParkedVehicle();
                    if(agv != null && agv.getIsAvailable() && !agvQueue.contains(agv))
                    {
                        //agv.followRoute(route);
                        return agvSpots.get(i);
                    }
                }
                break;
        }
        throw new NoFreeAgvException("No free AGV available");
    }
    
    public void loadContainerInAgv(AGV agv, Container container, TransportType tt)
    {
        // last thing to do, send to platform (identifiable with tt)
        //agv.followRoute();
    }
    
    private void createStrips() 
    {
        Vector3f stripPosition;
        for(int i = 0; i < getStripAmount(); i++)
        {
            stripPosition = new Vector3f(13.4f, getPosition().y, 3.15f + (i * 2.5f));
            strips[i] = new StorageStrip(this, stripPosition, i);
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
    
    public final int getStripAmount()
    {
        return (int)((float)LENGTH / STRIP_WIDTH);
    }
    
    public int getNearbyStrip(TransportType tt)
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
    
    public Vector3f getEntrycorner(Side side)
    {
        if(side.equals(Side.LEFT))
            return entrycorners[0];
        return entrycorners[1];
    }
    
    public Vector3f getExitcorner(Side side)
    {
        if(side.equals(Side.LEFT))
            return exitcorners[0];
        return exitcorners[1];
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
        entrypoints[0] = new Vector3f(getPosition().x + 1.7f, getPosition().y, getPosition().z + LENGTH);
        entrypoints[1] = new Vector3f(717f*Settings.METER, getPosition().y, getPosition().z + LENGTH);
    }
    
    private void setEntrycorners()
    {
        entrycorners = new Vector3f[2];
        entrycorners[0] = new Vector3f(getPosition().x + 1.7f, getPosition().y, getPosition().z + LENGTH);
        entrycorners[1] = new Vector3f(710f*Settings.METER - 1.7f, getPosition().y, getPosition().z + LENGTH);
    }
    
    private void setExitcorners()
    {
        exitcorners = new Vector3f[2];
        exitcorners[0] = new Vector3f(getPosition().x + 1.7f, getPosition().y, getPosition().z);
        exitcorners[1] = new Vector3f(710f*Settings.METER - 1.7f, getPosition().y, getPosition().z);
    }
    
    private void setExitpoints()
    {
        exitpoints = new Vector3f[2];
        exitpoints[0] = new Vector3f(getPosition().x + 1.7f, getPosition().y, getPosition().z);
        exitpoints[1] = new Vector3f(717f*Settings.METER, getPosition().y, getPosition().z);
    }
    
    public StoragePlatformOrientation getLeft() {
        return orientation.get(1);
    }
    
    public StoragePlatformOrientation getRight() {
        return orientation.get(0);
    }
    
    @Override
    public Vector3f getEntrypoint()
    {
        return getEntrypoint(Side.RIGHT);
    }
    
    @Override
    public Vector3f getExitpoint()
    {
        return getExitpoint(Side.RIGHT);
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
        time++;
        for(AGV agv : agvs)
            agv.update();
        for (StorageStrip strip : strips) {
            strip.update();
        }
    }
    
}