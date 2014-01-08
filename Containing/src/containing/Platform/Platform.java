package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Controller;
import containing.Dimension2f;
import containing.Exceptions.AgvNotAvailable;
import containing.Exceptions.AgvQueueSpaceOutOfBounds;
import containing.Exceptions.AgvSpotOutOfBounds;
import containing.Exceptions.CargoOutOfBoundsException;
import containing.Exceptions.ContainerNotFoundException;
import containing.Exceptions.NoFreeAgvException;
import containing.Exceptions.NoJobException;
import containing.Exceptions.VehicleOverflowException;
import containing.Job;
import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.ParkingSpot;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Train;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Platform implements Serializable {
    
    public enum State { FREE, LOAD, UNLOAD }
    public enum Positie { BOVEN, RECHTS, ONDER, LINKS };
    public enum DynamicAxis { X, Z }
    
    protected final float AGVSPOT_OFFSET = 0f;
    
    private static int idCounter = 1;
    
    private final int id;
    private final Vector3f position;
    private Dimension2f dimension;
    protected State state;
    protected DynamicAxis axis;
    private Vector3f entrypoint = null;
    private Vector3f entrycorner = null;
    private Vector3f exitpoint = null;
    private Vector3f exitcorner = null;
    private TransportType transportType = null;
    public Positie positie;
    
    protected List<AgvSpot> agvSpots;
    protected List<Crane> cranes;
    protected List<ParkingSpot> extVehicleSpots;
    protected List<ExternVehicle> extVehicles;
    protected Road road = null;
    
    protected Queue<Job> jobs = null;
    protected Queue<AGV> agvQueue = null; //is de bedoeling dat er een wachtrij van AGV's ontstaat
    protected int agvCount = 0;
    protected int maxAgvQueue = 1;    
    
    protected List<Crane> busyCranes;
    protected List<AGV> craneAgvs;
    
    protected int time = 0;
    
    public Platform(Vector3f position, Positie positie) 
    {
        id = idCounter++;
        this.position = position;
        state = State.FREE;
        /* initialize arraylists */
        agvSpots = new ArrayList<>();
        cranes = new ArrayList<>();
        extVehicleSpots = new ArrayList<>();
        extVehicles = new ArrayList<>();
        jobs = new LinkedList<>();
        agvQueue = new LinkedList<>();
        busyCranes = new ArrayList<>();
        craneAgvs = new ArrayList<>();
        this.positie = positie;
    }
    
    public List<ExternVehicle> getEvs()
    {
        return extVehicles;
    }
    
    public Road getRoad()
    {
        return road;
    }
    
    protected void setRoad()
    {
        List<Vector3f> wayshit = new ArrayList<>();
        wayshit.add(entrypoint);
        //wayshit.add(entrycorner);
        //wayshit.add(exitcorner);
        wayshit.add(exitpoint);
        road = new Road(wayshit);
    }
    
    public void registerExternVehicle(ExternVehicle ev)
    {
        try {
            extVehicles.add(ev);
            System.out.println("extVehicleSpot.position().x: " + extVehicleSpots.get(0).getPosition().x);
            ev.followRoute(road.getPathExternVehicleEntry(ev, extVehicleSpots.get(0)));
        } catch (AgvNotAvailable ex) {
            Logger.getLogger(Platform.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Crane> getCranes() 
    {
        return cranes;
    }
    
    protected boolean hasExtVehicle()
    {
        for(ParkingSpot vs : extVehicleSpots)
            if(!vs.isEmpty())
                return true;
        return false;
    }
    
    public void load(final Platform instance)
    {
        /* platform -> requestNextContainer -> agv from storagePlatform -> crane -> externVehicle */
        Crane craneTemp = null;
        for(Crane c : cranes)
        {
            if(c.getIsAvailable())
                craneTemp = c;
        }
        /*
        final Crane crane = craneTemp;
        new Thread() {
            
            @Override
            public void run()
            {
                int index = 0;
                boolean hasContainer = false;
                for(int i = 0; i < jobs.peek().getContainers().size(); i++)
                {
                    if(Controller.RequestNextContainer(jobs.peek().getContainers().get(index), instance))
                    {
                        hasContainer = true;
                        break;
                    }
                    index++;
                }
                
                if(hasContainer)
                {
                    Container container = jobs.peek().getContainers().remove(index);
                    try
                    {
                        crane.load(container);
                        extVehicleSpots.get(0).getParkedVehicle().load(crane.unload());
                        Settings.messageLog.AddMessage("loading some containers in some vehicle boi");
                    }
                    catch(ContainerNotFoundException | CargoOutOfBoundsException | VehicleOverflowException e)
                    {
                        System.out.println(e.getMessage());
                        this.interrupt();
                    }
                    
                }
            }
     
        }.start();
        */
    }
    
    public void unload()
    {   
        if(extVehicles.isEmpty()) {
            for(int i = 0; i < extVehicleSpots.size(); i++) 
            {
                if(extVehicleSpots.get(i).getParkedVehicle() != null) {
                    extVehicles.add((Train)extVehicleSpots.get(i).getParkedVehicle());
                }
            }
        }
    }
    
    protected void unloadAGV(Container container)
    {
        //todo
    }
    
    protected void requestNextContainer()
    {
        //Controller.RequestNextContainer(null, this);
    }
    
    protected void requestNextJob()
    {
        try 
        {
            jobs.add(Controller.RequestNewJob(this));
        } 
        catch(NoJobException e){/* ignore */}
    }
    
    private void agvToQueue(AGV agv) throws AgvQueueSpaceOutOfBounds
    {
        if(agvQueue.size() < maxAgvQueue)
            agvQueue.add(agv);
        else
            throw new AgvQueueSpaceOutOfBounds("No space available for AGV in agvQueue");
    }
    
    protected void addAgvToQueue(AGV agv)
    {
        try
        {
            agvToQueue(agv);
        }
        catch(AgvQueueSpaceOutOfBounds e){/* ignore */}
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
    
    protected int getAgvSpotAmount()
    {
        return (int)((float)(axis.equals(DynamicAxis.X) ? dimension.width : dimension.length) / AgvSpot.width);
    }
    
    protected AgvSpot getAgvSpot(int spot) throws AgvSpotOutOfBounds
    {
        if(agvSpots.size() > spot)
            return agvSpots.get(spot);
        throw new AgvSpotOutOfBounds("The requested AGV Spot does not exist");
    }
    
    public boolean hasFreeParkingSpot()
    {
        for(ParkingSpot spot : extVehicleSpots)
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
    
    public Vector3f getEntrycorner()
    {
        return entrycorner;
    }
    
    protected void setEntrycorner(Vector3f entrycorner)
    {
        this.entrycorner = entrycorner;
    }
    
    public Vector3f getExitpoint() {
        return exitpoint;
    }
    
    protected void setExitpoint(Vector3f exitpoint)
    {
        this.exitpoint = exitpoint;
    }
    
    public Vector3f getExitcorner()
    {
        return exitcorner;
    }
    
    protected void setExitcorner(Vector3f exitcorner)
    {
        this.exitcorner = exitcorner;
    }
    
    public TransportType getTransportType() {
        return transportType;
    }
    
    protected void setTransportType(TransportType transportType)
    {
        this.transportType = transportType;
    }
    
    protected void setMaxAgvQueue(int max)
    {
        //calculate how much AGV's fit in queue
        maxAgvQueue = max;
    }
    
    public int getId()
    {
        return id;
    }
    
    protected abstract void createCranes();
    protected abstract void createExtVehicleSpots();
    
    public void update()
    {
        for(Crane c : cranes)
            c.update();
        for(ExternVehicle ev : extVehicles)
            ev.update();
    }
    
    @Override
    public String toString() {
        return String.format("[%d, width=%.1f, length=%.1f]", id, dimension.width*10f, dimension.length*10f);
    }
    
    protected void log(String msg)
    {
        Settings.messageLog.AddMessage(msg);
    }
    
}