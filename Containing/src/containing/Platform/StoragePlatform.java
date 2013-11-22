package containing.Platform;

import containing.ParkingSpot.AgvSpot;
import containing.ParkingSpot.ParkingSpot;
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
    
    private final int NR_STRIPS = 64;       // amount of StorageStrip objects
    private final StorageStrip[] strips;

    public StoragePlatform(int id, int nrAgvSpots, int nrCranes) {
        super(id, nrAgvSpots, nrCranes);
        // initialize strips
        strips = new StorageStrip[NR_STRIPS];
        for(int i = 0; i < NR_STRIPS; i++)
            strips[i] = new StorageStrip(i, this);
        // initialize cranes
        Arrays.fill(cranes, new StorageCrane());
    }

}