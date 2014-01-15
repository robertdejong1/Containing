package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import static containing.Container.TransportType.Train;
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
import containing.Vehicle.Train;
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
            for(int i=0; i < extVehicleSpots.size(); i++) {
                if(extVehicleSpots.get(i).getParkedVehicle() == null) {
                    System.out.println("extVehicleSpot.position().x: " + extVehicleSpots.get(i).getPosition().x);
                    ev.followRoute(road.getPathExternVehicleEntry(ev, extVehicleSpots.get(i)));
                }
            }
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

    protected int getColumn(int currentCrane, int rowsPerCrane, ExternVehicle ev, int cranesPerVehicle) 
    {
        List<Integer> pColumns = ev.getPriorityColumns();
        List<Boolean> columns = ev.getColumns();
        int startIndex = (cranesPerVehicle-1 - currentCrane) * rowsPerCrane; 
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
            if(ev instanceof Train) {
                Collections.reverse(unloadOrder);
            }
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
        System.out.println("busyCranes contains? " + busyCranes.contains(c));
        System.out.println("craneStatus? " + c.getStatus());
        System.out.println("moveToColumn? " + moveToColumn);
        System.out.println("container is null? " + (getContainer(moveToColumn, ev) == null ? "yes" : "no"));
        if(!busyCranes.contains(c) && craneAgv == null && moveToColumn != -1 && c.getStatus() == Vehicle.Status.WAITING) 
        {
            return Phase.MOVE;
        } 
        else if(busyCranes.contains(c) && c.getStatus() == Vehicle.Status.WAITING && craneAgv == null && getContainer(moveToColumn, ev) != null) 
        {
            return Phase.LOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Vehicle.Status.MOVING && c.getStatus() == Vehicle.Status.UNLOADING && !c.getCargo().isEmpty() && !agvSpots.get(currentCrane).isEmpty()) 
        {
            return Phase.UNLOAD;
        }
        else if(busyCranes.contains(c) && craneAgv != null && craneAgv.getStatus() != Vehicle.Status.MOVING && c.getStatus() == Vehicle.Status.WAITING && c.getCargo().isEmpty()) 
        {
            return Phase.SENDTOSTORAGE;
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
     * UNLOAD PHASE : Unload container to AGV
     * @param currentCrane nr crane
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
    
    /**
     * UNLOAD PHASE : Send loaded AGV to StoragePlatform
     * @param currentCrane nr crane
     */
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
    
    /**
     * Return all cranes on this platform
     * @return list of cranes 
     */
    public List<Crane> getCranes() 
    {
        return cranes;
    }
    
    /**
     * Check if there is one or more extern vehicles
     * @return true or false
     */
    protected boolean hasExtVehicle()
    {
        for(ParkingSpot vs : extVehicleSpots)
            if(!vs.isEmpty())
                return true;
        return false;
    }
    
    /**
     * TODO
     * @param instance 
     */
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
    
    /**
     * Unload basics needed on every platform, add Extern vehicles
     * to 'to unload' list
     */
    public void unload()
    {   
        for(int i = 0; i < extVehicleSpots.size(); i++) 
        {
            if(extVehicleSpots.get(i).getParkedVehicle() != null) {
                if(!extVehicles.contains((ExternVehicle)extVehicleSpots.get(i).getParkedVehicle()))
                    extVehicles.add((ExternVehicle)extVehicleSpots.get(i).getParkedVehicle());
            }
        }
    }
    
    /**
     * TODO
     */
    protected void requestNextContainer()
    {
        //Controller.RequestNextContainer(null, this);
    }
    
    /**
     * TODO
     */
    protected void requestNextJob()
    {
        try 
        {
            jobs.add(Controller.RequestNewJob(this));
        } 
        catch(NoJobException e){/* ignore */}
    }
    
    /**
     * Add AGV to queue, which is essentially a wait list
     * of AGVS which are get loaded by cranes
     * @param agv the agv to be loaded
     */
    protected void addAgvToQueue(AGV agv)
    {
        if(agvQueue.size() < maxAgvQueue)
            agvQueue.add(agv);
    }
    
    /**
     * Create parkingspot for AGV's to park at the cranes.
     * @param baseposition the first position
     */
    protected void createAgvSpots(Vector3f baseposition)
    {   
        float x = baseposition.x;
        float z = baseposition.z;
        
        for(int i = 0; i < getAgvSpotAmount(); i++)
        {
            Vector3f spotPosition;
            float currentWidth = AgvSpot.width*i;
            spotPosition = axis.equals(DynamicAxis.X) ? new Vector3f(currentWidth,5.5f,z) : new Vector3f(x,5.5f,currentWidth);
            agvSpots.add(new AgvSpot(spotPosition));
        }
    }
    
    /**
     * Return the amount of AGV spots
     * @return amount of agv spots
     */
    protected int getAgvSpotAmount()
    {
        return (int)((float)(axis.equals(DynamicAxis.X) ? dimension.width : dimension.length) / AgvSpot.width);
    }
    
    /**
     * Return requested AGV spot
     * @param spot nr of spot
     * @return parkingspot for AGV
     * @throws AgvSpotOutOfBounds 
     */
    protected AgvSpot getAgvSpot(int spot) throws AgvSpotOutOfBounds
    {
        if(agvSpots.size() > spot)
            return agvSpots.get(spot);
        throw new AgvSpotOutOfBounds("The requested AGV Spot does not exist");
    }
    
    /**
     * Check if there is a free parking spot on this platform
     * @return true or false
     */
    public boolean hasFreeParkingSpot()
    {
        for(ParkingSpot spot : extVehicleSpots)
            if(spot.isEmpty())
                return true;
        return false;
    }
    
    /**
     * Return position of this platform
     * @return list of 3 floats
     */
    public Vector3f getPosition()
    {
        return position;
    }
    
    /**
     * Return the dimension of this platform
     * @return list of 2 floats
     */
    public Dimension2f getDimension() 
    {
        return dimension;
    }
    
    /**
     * Set the dimension of this platform
     * @param dimension 2 floats
     */
    protected void setDimension(Dimension2f dimension)
    {
        this.dimension = dimension;
    }
    
    /**
     * Return the axis the cranes move on
     * @return axis
     */
    public DynamicAxis getAxis()
    {
        return axis;
    }
    
    /**
     * Set the axis the cranes move on
     * @param axis x or z
     */
    protected void setAxis(DynamicAxis axis) 
    {
        this.axis = axis;
    }
    
    /**
     * Return the entrypoint of the road
     * @return list of 3 points
     */
    public Vector3f getEntrypoint()
    {
        return entrypoint;
    }
    
    /**
     * Set entrypoint of the road
     * @param entrypoint
     */
    protected void setEntrypoint(Vector3f entrypoint)
    {
        this.entrypoint = entrypoint;
    }
    
    /**
     * Return the exitpoint of the road
     * @return list of 3 points 
     */
    public Vector3f getExitpoint() {
        return exitpoint;
    }
    
    /**
     * Set exitpoint of the road
     * @param exitpoint
     */
    protected void setExitpoint(Vector3f exitpoint)
    {
        this.exitpoint = exitpoint;
    }
    
    /**
     * Return the type of transport (Extern Vehicle)
     * @return transportType
     */
    public TransportType getTransportType() {
        return transportType;
    }
    
    /**
     * Set the type of transport (Extern Vehicle)
     * @param transportType 
     */
    protected void setTransportType(TransportType transportType)
    {
        this.transportType = transportType;
    }
    
    /**
     * Set the maximum of AGVS which are allowed in the waitlist (queue)
     * @param max nr of AGVs
     */
    protected void setMaxAgvQueue(int max)
    {
        //calculate how much AGV's fit in queue
        maxAgvQueue = max;
    }
    
    /**
     * Return the ID which is assigned to this platform
     * @return ID
     */
    public int getId()
    {
        return id;
    }
    
    protected abstract void createCranes();
    protected abstract void createExtVehicleSpots();
    
    /**
     * Update is called every 100ms
     */
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