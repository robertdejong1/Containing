package containing.Platform;

import containing.Container.TransportType;
import containing.Dimension2f;
import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.ParkingSpot;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.Crane;
import java.util.ArrayList;
import java.util.List;

public abstract class Platform {
    
    public enum State { BUSY, FREE }
    protected enum DynamicAxis { X, Z }
    
    private final float AGVSPOT_OFFSET = 0f;
    
    private static int idCounter = 1;
    
    private final int id;
    private final Vector3f position;
    private Dimension2f dimension;
    private DynamicAxis axis;
    private Vector3f entrypoint = null;
    private Vector3f exitpoint = null;
    private TransportType transportType = null;
    
    protected List<AgvSpot> agvSpots;
    protected List<Crane> cranes;
    protected List<ParkingSpot> extVehicleSpots;
    
    public Platform(Vector3f position) 
    {
        id = idCounter++;
        this.position = position;
        /* initialize arraylists */
        agvSpots = new ArrayList<>();
        cranes = new ArrayList<>();
        extVehicleSpots = new ArrayList<>();
    }
    
    protected void createAgvSpots(Vector3f baseposition)
    {   
        float x = baseposition.x;
        float z = baseposition.z;
        
        for(int i = 0; i < getAgvSpotAmount(); i++)
        {
            Vector3f spotPosition;
            float currentWidth = AgvSpot.width*i+AGVSPOT_OFFSET;
            spotPosition = axis.equals(DynamicAxis.X) ? new Vector3f(currentWidth,0,z) : new Vector3f(x,0,currentWidth);
            agvSpots.add(new AgvSpot(spotPosition));
        }
    }
    
    private int getAgvSpotAmount()
    {
        return (int)((float)(axis.equals(DynamicAxis.X) ? dimension.width : dimension.length) / AgvSpot.width);
    }
    
    public boolean hasFreeParkingSpot()
    {
        for(AgvSpot spot : agvSpots)
            if(spot.isEmpty())
                return true;
        return false;
    }
    
    public Vector3f getPosition()
    {
        return position;
    }
    
    public Dimension2f getDimension() 
    {
        return dimension;
    }
    
    protected void setDimension(Dimension2f dimension)
    {
        this.dimension = dimension;
    }
    
    public DynamicAxis getAxis()
    {
        return axis;
    }
    
    protected void setAxis(DynamicAxis axis) 
    {
        this.axis = axis;
    }
    
    public Vector3f getEntrypoint()
    {
        return entrypoint;
    }
    
    protected void setEntrypoint(Vector3f entrypoint)
    {
        this.entrypoint = entrypoint;
    }
    
    public Vector3f getExitpoint() {
        return exitpoint;
    }
    
    protected void setExitpoint(Vector3f exitpoint)
    {
        this.exitpoint = exitpoint;
    }
    
    public TransportType getTransportType() {
        return transportType;
    }
    
    protected void setTransportType(TransportType transportType)
    {
        this.transportType = transportType;
    }
    
    public int getId()
    {
        return id;
    }
    
    protected abstract void createCranes();
    protected abstract void createExtVehicleSpots();
    
    public abstract void update();
    
    @Override
    public String toString() {
        return String.format("[%d, width=%.1f, length=%.1f]", id, dimension.width, dimension.length);
    }
    
    protected void log(String msg)
    {
        Settings.messageLog.AddMessage(msg);
    }
    
}