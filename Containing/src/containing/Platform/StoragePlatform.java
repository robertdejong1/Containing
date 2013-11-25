package containing.Platform;

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
    private final float WIDTH = 600;        // the width of this platform
    private final float LENGTH = 1550;      // the lenght of this platform
    private final StorageStrip[] strips;    // ? still needed?
    private float stripZ;                   // CRANE_Z_POSITION
    
    /**
     * Create Storage platform
     * @param position the position in the port
     */
    public StoragePlatform(Vector3f position) {
        super(position);
        this.stripZ = LENGTH / 64;
        // initialize strips
        strips = new StorageStrip[NR_STRIPS];
        for(int i = 0; i < NR_STRIPS; i++) {
            Vector3f newPosition = new Vector3f(0,0,stripZ*i);
            strips[i] = new StorageStrip(i, newPosition, this);
        }
        // initialize cranes
        for(int i = 0; i < NR_STRIPS; i++) {
            Vector3f newPosition = new Vector3f(0,0, stripZ*i);
            cranes[i] = new StorageCrane(newPosition);
        }
    }
    
    /**
     * Special method ;). Create 100 AGV's with positions on this platform.
     * Port.java calls this function and gets all AGV's
     * @return 
     */
    public AGV[] createAllAgvs() {
        AGV[] agvs = new AGV[NR_AGVS];
        //TODO: Create AGV's on the strips
        return agvs;
    }
    
    @Override
    public void update() {
        
    }

}