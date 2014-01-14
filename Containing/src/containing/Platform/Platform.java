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
import containing.Road.Route;
import containing.Settings;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import containing.Vehicle.ExternVehicle;
import containing.Vehicle.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Platform implements Serializable {
    
    public enum Phase { MOVE, LOAD, UNLOAD, SENDTOSTORAGE }
    public enum State { FREE, LOAD, UNLOAD }
    public enum Positie { BOVEN, RECHTS, ONDER, LINKS };
    public enum DynamicAxis { X, Z }
    
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
    protected Queue<AGV> agvQueue = null;
    protected int agvCount = 0;
    protected int maxAgvQueue = 1;    
    
    protected List<Crane> busyCranes;
    protected List<AGV> craneAgvs;
    protected List<Vector3f> agvQueuePositions;
    protected Road craneRoad = null;
    
    protected boolean unloadOnce = false;
    
    protected int time = 0;
    
    /**
     * The Platform class defines the basics of a platform
     * @param position the position in the Port
     * @param positie the position in the Port (left, right, top, bottom)
     */
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
    
    /**
     * Return extern vehicles which are docked on the
     * platform
     * @return a list with docked extern vehicles
     */
    public List<ExternVehicle> getEvs()
    {
        return extVehicles;
    }
    
    /**
     * Return the road of this platform, the AGV's are the
     * only vehicles which are driving on it.
     * @return the road
     */
    public Road getRoad()
    {
        return road;
    }
    
    /**
     * Set the road for AGV's with 2 points
     */
    protected void setRoad()
    {
        List<Vector3f> wayshit = new ArrayList<>();
        wayshit.add(entrypoint);
        wayshit.add(exitpoint);
        road = new Road(wayshit);
    }
    
    /**
     * Return the road for the cranes on this platform.
     * @return the crane road
     */
    protected Road getCraneRoad() { 
        return craneRoad;
    }
    
    /**
     * Set the road for cranes on this platform
     * @param waypoints 2 points which define the road
     */
    protected void setCraneRoad(List<Vector3f> waypoints) {
        craneRoad = new Road(waypoints);
    }
    
    /**
     * Register extern vehicle at platform, the EV is given a route
     * to the parkingspot where it can dock.
     * @param ev the extern vehicle which is registerd
     */
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
    
    /**
     * Send AGV's to the platform, when there is a extern vehicle,
     * which needs to be unloaded, creates a queue of AGV's whom are
     * waiting for a loaded crane
     * @param cargoSize the current nr of cargo on the extern vehicle
     * @param positions the positions where an AGV can be 'parked' temporarily
     */
    protected void sendAgvs(int cargoSize, List<Vector3f> positions) {
        if(agvQueue.isEmpty() || agvQueue.size() < maxAgvQueue) {
            for(int i = agvQueue.size(); i < (cargoSize < maxAgvQueue ? cargoSize : maxAgvQueue); i++) {
                if(time >= 30) {
                    try {
                        AgvSpot agvSpot = Settings.port.getStoragePlatform().requestFreeAgv(getTransportType(), agvQueue);
                        AGV agv = (AGV)agvSpot.getParkedVehicle();
                        try {
                            agv.followRoute(road.getPathAllInVector(agv, agvSpot, positions.get(i), this, Settings.port.getMainroad()));
                        } catch (AgvNotAvailable ex) {
                            Logger.getLogger(TrainPlatform.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        addAgvToQueue(agv);
                    } catch(NoFreeAgvException e) {
                        System.out.println("No Free AGV available ;(");
                    }
                    time = 0;
                    break;
                }
            }
        }
    }
    
    /**
     * Get column of containers from Extern Vehicle
     * @param currentCrane the crane who requested
     * @param rowsPerCrane the amount of rows the crane is allowed to unload
     * @param ev the extern vehicle
     * @return column nr
     */
    protected int getColumn(int currentCrane, int rowsPerCrane, ExternVehicle ev) 
    {
        List<Integer> pColumns = ev.getPriorityColumns();
        List<Boolean> columns = ev.getColumns();
        int startIndex = currentCrane * rowsPerCrane;
        for(int i = startIndex; i < startIndex + rowsPerCrane; i++)
        {
            if(pColumns.contains(i) && !columns.get(i))
                return i;
            else if(!columns.get(i))
                return i;
        }
        return -1;
    }
    
    /**
     * Get container from column
     * @param column
     * @param ev
     * @return container
     */
    protected Container getContainer(int column, ExternVehicle ev) {
        List<Integer> unloadOrder = ev.getUnloadOrderY(column);
        if(unloadOrder != null)
        {
            //System.out.println("unloadOrder == " +  unloadOrder.size());
            Collections.reverse(unloadOrder);
            int rowUnloaded = 0;
            for(Integer row : unloadOrder) 
            {
                for(Container container : ev.getGrid()[column][row]) 
                {
                    if(container != null) 
                    {
                        return container;
                    } 
                    else 
                    {
                        rowUnloaded++;
                        if(rowUnloaded == unloadOrder.size()) 
                        {
                            ev.getColumns().set(column, true);
                            ev.updateColumn(column, true);
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Determines the phase of which a crane is in for unloading
     * @param currentCrane crane nr
     * @param moveToColumn column nr
     * @param ev extern vehicle
     * @return the phase
     */
    protected Phase unload_getPhase(int currentCrane, int moveToColumn, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        AGV craneAgv = craneAgvs.get(currentCrane);
        if(!busyCranes.contains(c) && craneAgv == null && moveToColumn != -1 && c.getStatus() == Vehicle.Status.WAITING) 
        {
            return TrainPlatform.Phase.MOVE;
        } 
        else if(busyCranes.contains(c) && c.getStatus() == Vehicle.Status.WAITING && craneAgv == null && getContainer(moveToColumn, ev) != null) 
        {
            return TrainPlatform.Phase.LOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Vehicle.Status.MOVING && c.getStatus() == Vehicle.Status.UNLOADING && !c.getCargo().isEmpty() && !agvSpots.get(currentCrane).isEmpty()) 
        {
            return TrainPlatform.Phase.UNLOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Vehicle.Status.MOVING && c.getStatus() == Vehicle.Status.WAITING && c.getCargo().isEmpty()) 
        {
            return TrainPlatform.Phase.SENDTOSTORAGE;
        }
        return null;
    }
    
    /**
     * UNLOAD PHASE : Move crane to the column on the extern vehicle
     * @param currentCrane crane nr
     * @param column column nr
     * @param ev extern vehicle
     */
    protected void unload_phaseMove(int currentCrane, int column, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        if(ev.getGrid()[column][0][0] != null) {
            try {
                Route ding = craneRoad.moveToContainer(ev, column, c);
                System.out.println("Ja route is dit " +ding.getWeg());
                c.followRoute(ding);
                busyCranes.add(c);
            } catch (AgvNotAvailable ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * UNLOAD PHASE : Load container from extern vehicle onto the crane
     * @param currentCrane crane nr
     * @param container the container which is going to load onto crane
     * @param ev extern vehicle
     */
    protected void unload_phaseLoad(int currentCrane, Container container, ExternVehicle ev) 
    {
        Crane c = cranes.get(currentCrane);
        // adjust parkingspot
        Vector3f cp = c.getPosition();
        agvSpots.set(currentCrane, new AgvSpot(new Vector3f(cp.x + c.length*Settings.METER, cp.y, cp.z)));
        // send AGV from queue
        AGV agv = agvQueue.peek();
        if(agv.getStatus() != Vehicle.Status.MOVING) {
            try {
                agv = agvQueue.poll();
                System.out.println("AGV == " + agv.getStatus());
                agv.followRoute(road.getPathToParkingsSpot(agv, agvSpots.get(currentCrane)));
                System.out.println("AGV == " + agv.getStatus());
                System.out.println("breakie breakie");
                //while(true) {}
                craneAgvs.set(currentCrane, agv);
                if(c.getStatus() == Vehicle.Status.WAITING) {
                    try {
                        c.load(container, ev);
                    } catch (CargoOutOfBoundsException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            } catch (AgvNotAvailable ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    /**
     * UNLOAD PHASE : 
     * @param currentCrane 
     */
    protected void unload_phaseUnload(int currentCrane) 
    {
        Crane c = cranes.get(currentCrane);
        AGV craneAgv = craneAgvs.get(currentCrane);
        System.out.println("(UNLOAD) AGV STATUS == " + craneAgv.getStatus());
        if(!unloadOnce) {
            try {
                System.out.println("crane cargo == " + c.getCargo().size());
                c.unload(craneAgv);
                unloadOnce = true;
            } catch (VehicleOverflowException | ContainerNotFoundException | CargoOutOfBoundsException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    protected void unload_phaseSendToStorage(int currentCrane) 
    {
        try {
            Crane c = cranes.get(currentCrane);
            AGV craneAgv = craneAgvs.get(currentCrane);
            busyCranes.remove(c);
            unloadOnce = false;
            craneAgvs.set(currentCrane, null);
            agvSpots.get(currentCrane).UnparkVehicle();
            // check where container needs to go
            //TransportType tt = craneAgv.getCargo().get(0).getDepartureTransport();
            int stripNr = Settings.port.getStoragePlatform().getNearbyStrip(getTransportType());
            AgvSpot agvSpot = null;
            for(int i = stripNr; i < Settings.port.getStoragePlatform().getStripAmount(); i++)
            {
                if(Settings.port.getStoragePlatform().getStrip(i).getStorageState() != StorageStrip.StorageState.FULL)
                {
                    agvSpot = Settings.port.getStoragePlatform().getStrip(i).getFreeAgvSpotLoad();
                }
                if(agvSpot != null)
                    break;
            }
            System.out.println("WOW WTF GEBEURT JIR");
            Settings.port.getStoragePlatform().putAgvQueueLoadBusy(agvSpot);
            craneAgv.followRoute(road.getPathAllIn(craneAgv, agvSpots.get(currentCrane), agvSpot, Settings.port.getStoragePlatform().getLeft(), Settings.port.getMainroad()));
        } catch (AgvNotAvailable ex) {
            System.out.println(ex.getMessage());
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
        //TODO BLABLA
    }
    
    public void unload()
    {   
        if(extVehicles.isEmpty()) {
            for(int i = 0; i < extVehicleSpots.size(); i++) 
            {
                if(extVehicleSpots.get(i).getParkedVehicle() != null) {
                    
                    extVehicles.add((ExternVehicle)extVehicleSpots.get(i).getParkedVehicle());
                }
            }
        }
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
            float currentWidth = AgvSpot.width*i;
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