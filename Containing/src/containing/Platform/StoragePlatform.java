package containing.Platform;

import containing.Vector3f;
import containing.Vehicle.AGV;
import containing.Vehicle.StorageCrane;
import java.util.Arrays;

/**
 * Dit de controller voor individuele StorageStrips.
 * De controller stuurt bijvoorbeeld een AVG naar StoragePlatform.
 * Nu mag dit platform het gaan afhandelen.
 * Het platform geeft aan waar de AVG's naar toe moeten.
 * Er zijn 64 stroken met elk 12 parkeerplaatsen.
 * @see Technisch Document
 * @author Minardus
 */

public class StoragePlatform extends Platform {
    
    private int time;
    private final int NR_AGVS = 100;        // number of AGV's in the port
    private final int NR_STRIPS = 64;       // amount of StorageStrip objects
    private final StorageStrip[] strips;
    
    /**
     * Create Storage platform
     * @param position the position in the port
     */
    public StoragePlatform(Vector3f position) {
        super(position);
        // initialize strips
        strips = new StorageStrip[NR_STRIPS];
        for(int i = 0; i < NR_STRIPS; i++)
            strips[i] = new StorageStrip(i, this);
        // initialize cranes
        Arrays.fill(cranes, new StorageCrane());
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