package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Job;
import containing.ParkingSpot.ParkingSpot;
import containing.ParkingSpot.AgvSpot;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.Crane;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Platform class, has the basic functions for all Platforms in the port.
 * @author Minardus
 */

public abstract class Platform {
    
    private static int idCounter = 0;       // used for automatic ID generation
    
    // non-access properties
    private final int id;                   // unique ID for identifying platform
    private final Vector3f position;        // the position of this platform (for simulation)
    private Dimension2f dimension;          // dimension of platform (width x length)
    private Vector3f entrypoint;            // waypoint of entrance
    private Vector3f exitpoint;             // waypoint of exit
    
    // accessable properties (in extended classes)
    protected ParkingSpot[] agvSpots;       // all parking spots for AGV's
    protected Crane[] cranes;               // the cranes on the platform
    protected ParkingSpot[] vehicleSpots;   // spot of external vehicle
    
    // jobs todo
    protected Queue<Job> jobQueue;
    
    // timing
    protected int timing = 0;
    
    /**
     * Give platform unique ID and position
     * @param position The position of the platform in the port
     */
    public Platform(Vector3f position) {
        this.id = ++idCounter;
        this.position = position;
        jobQueue = new LinkedList<>();
    }
    
    public void initAgvSpots(char axis) {
        // initialize parking spots for AGV's
        Vector3f agvSpotBasePosition = new Vector3f(0,0,0);
        int nrAgvSpots = (int)((axis == 'x' ? dimension.width : dimension.length) / ParkingSpot.width);
        agvSpots = new AgvSpot[nrAgvSpots];
        for(int i = 0; i < agvSpots.length; i++) 
        {
            float newX = agvSpotBasePosition.x + ParkingSpot.width;
            float newZ = agvSpotBasePosition.z + ParkingSpot.length;
            Vector3f newAgvSpotPosition = new Vector3f(newX, 0, newZ);
            agvSpots[i] = new AgvSpot(newAgvSpotPosition);
        }
    }
    
    /**
     * Set dimension of platform and the waypoints of entrance and exit
     * @param dimension width x length of platform
     * @param entrypoint the waypoint of the entrance
     * @param exitpoint the waypoint of the exit
     */
    protected void setDimensionAndWayPoints(Dimension2f dimension, Vector3f entrypoint, Vector3f exitpoint) {
        this.dimension = dimension;
        this.entrypoint = entrypoint;
        this.exitpoint = exitpoint;
    }
    
    public void pushJob(Job job) {
        
    }
    
    /**
     * Park a empty AGV
     * @param agv The AGV that is sended by controller 
     */
    public void parkAGV(AGV agv) {
        agvSpots[getFreeParkingSpot()].ParkVehicle(agv);
    }
    
    /**
     * Park a AGV that is assigned to a container
     * @param agv The AGV that is sended by controller
     * @param container The container thats gonna be loaded/unloaded
     */
    public void parkAGV(AGV agv, Container container) {
        agvSpots[getFreeParkingSpot()].ParkVehicle(agv);
    }
    
    public int getFreeParkingSpot() {
        for(int i = 0; i < agvSpots.length; i++) {
            if(agvSpots[i].isEmpty())
                return i;
        }
        return 0;
    }
    
    public ParkingSpot getAgvSpot(int spot) {
        return agvSpots[spot];
    }
    
    public ParkingSpot[] getAllAgvSpots() {
        return agvSpots;
    }
    
    public ParkingSpot getVehicleSpot(int spot) {
        return vehicleSpots[spot];
    }
    
    public ParkingSpot[] getAllVehicleSpots() {
        return vehicleSpots;
    }
    
    public int getId() {
        return id;
    }
    
    public Dimension2f getDimension() {
        return dimension;
    }
    
    public Vector3f getPosition() {
        return position;
    }
    
    public Vector3f entryPoint() {
        return entrypoint;
    }
    
    public Vector3f exitPoint() {
        return exitpoint;
    }
    
    @Override
    public String toString() {
        return String.format("[%d, width=%f, length=%f]", id, dimension.width, dimension.length);
    }
    
    protected abstract void initVehicleSpots();
    protected abstract void initCranes();
    public abstract TransportType getTransportType();
    
    public abstract void update();
    
}
