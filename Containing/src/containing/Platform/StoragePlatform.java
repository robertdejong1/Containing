package containing.Platform;

/**
 * Dit de controller voor individuele StorageStrips.
 * De controller stuurt bijvoorbeeld een AVG naar StoragePlatform.
 * Nu mag dit platform het gaan afhandelen.
 * Het platform geeft aan waar de AVG's naar toe moeten.
 * @see Technisch Document
 * @author Minardus
 */

public class StoragePlatform extends Platform {
    
    StorageStrip[] storageStrips;

    public StoragePlatform(int id, int nrAvgSpots) {
        super(id, nrAvgSpots);
    }

}