package containing.Platform;

import containing.Container;
import containing.Container.TransportType;
import containing.Dimension2f;
import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.StorageCrane;
import java.util.Arrays;

/**
 * StoragePlatform
 * 
 * Crane movement:
 * X-axis: dynamic
 * Z-axis: static
 * 
 * @author Minardus
 */

public class StoragePlatform extends Platform {
    
    private final int NR_AGVS = 100;        // number of AGV's in the port
    private final int NR_STRIPS = 64;       // amount of StorageStrip objects, also amount of cranes
    private final float X_SIZE = 600;       // the width of this platform
    private final float Z_SIZE = 1550;      // the lenght of this platform
    private final StorageStrip[] strips;    // ? still needed?
    private final float stripZ;             // CRANE_Z_POSITION
    private Container[][][][] containers;   // [stripId][x][y][z]
    
    /**
     * Create Storage platform
     * @param position the position in the port
     */
    public StoragePlatform(Vector3f position) {
        super(position);
        
        containers = new Container[NR_STRIPS][40][6][6];
        
        // width of 1 strip
        this.stripZ = Z_SIZE / 64;
        
        // set dimensions and entrance/exit waypoints of platform
        Dimension2f newDimension = new Dimension2f(X_SIZE, Z_SIZE);
        Vector3f newEntrypoint = new Vector3f(0,0,0);
        Vector3f newExitpoint = new Vector3f(0,0,0);
        setDimensionAndWayPoints(newDimension, newEntrypoint, newExitpoint);
        
        // initialize AGV spots
        initAgvSpots('x');
        
        // initialize strips
        strips = new StorageStrip[NR_STRIPS];
        for(int i = 0; i < NR_STRIPS; i++) {
            Vector3f newPosition = new Vector3f(0,0,stripZ*i);
            strips[i] = new StorageStrip(i, newPosition, this);
        }
        
        // initialize cranes
        initCranes();
    }
    
    /**
     * Special method ;). Create 100 AGV's with positions on this platform.
     * Port.java calls this function and gets all AGV's
     * @return 
     */
    public AGV[] getAllCreatedAgvs() {
        AGV[] agvs = new AGV[NR_AGVS];
        for(int i = 0; i < NR_AGVS; i++) {
            //agvs[i] = new AGV(this, agvSpots[i].getPosition());
            agvSpots[i].ParkVehicle(agvs[i]);
        }
        return agvs;
    }
    
    public boolean isContainerHere(Container container) {
        boolean isHere = false;
        return isHere;
    }
    
    /**
     * Not needed on this platform
     */
    @Override
    protected void initVehicleSpots() { }
    
    @Override
    protected final void initCranes() {
        cranes = new StorageCrane[NR_STRIPS];
        for(int i = 0; i < NR_STRIPS; i++) {
            Vector3f newPosition = new Vector3f(0,0, stripZ*i);
            cranes[i] = new StorageCrane(newPosition);
        }
    }
    
    @Override
    public TransportType getTransportType() {
        return null;
    }
    
    @Override
    public void update() {
        
    }

}