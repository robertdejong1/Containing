package containing.Platform;

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
    
    private final int NR_STRIPS = 64; 
    private StorageStrip[] storageStrips;

    public StoragePlatform(int id, int nrAgvSpots) {
        super(id, nrAgvSpots);
        initStorageStrips();
    }
    
    private void initStorageStrips() {
        //todo
    }

}