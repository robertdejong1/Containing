package containing.Platform;

/**
 * 1 Storage strip krijgt avgSpots
 * Aan de linkerkant wordt geladen, aan de rechterkant gelost.
 * Als ik de even avgSpots aan de linkerkant doe, en de oneven
 * aan de rechterkant kunnen we daar op checken.
 * @author Minardus
 */

public class StorageStrip {
    
    private final int id;
    private final StoragePlatform controller;
    
    public StorageStrip(int id, StoragePlatform controller) {
        this.id = id;
        this.controller = controller;
    }
    
    public int getId() {
        return id;
    }
    
}
