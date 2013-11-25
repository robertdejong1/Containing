package containing.Platform;

import containing.Container;
import containing.Vector3f;
import containing.Vehicle.AGV;
import java.util.ArrayList;
import java.util.List;

/**
 * 1 Storage strip krijgt avgSpots
 * Aan de linkerkant wordt geladen, aan de rechterkant gelost.
 * Als ik de even avgSpots aan de linkerkant doe, en de oneven
 * aan de rechterkant kunnen we daar op checken.
 * @author Minardus
 */

public class StorageStrip {
    
    private final int id;
    private final Vector3f position;
    private final StoragePlatform controller;
    private final Vector3f MAX_SPACE = new Vector3f(6, 40, 6);
    private Container[][][] containers;
    
    public StorageStrip(int id, Vector3f position, StoragePlatform controller) {
        this.id = id;
        this.position = position;
        this.controller = controller;
    }
    
    public void load(Container container) {
        Vector3f pos = getFreePosition();
        //containers[pos.x][pos.z][pos.y] = container;
    }
    
    public Container unload(Vector3f pos) {
        Container container = getContainer(pos);
        resetPosition(pos);
        return container;
    }
    
    public Vector3f getFreePosition() {
        Vector3f pos = new Vector3f(0,0,0);
        return pos;
    }
    
    public Container getContainer(Vector3f pos) {
        //return containers[pos.x][pos.z][pos.y];
        return null;
    }
    
    public void resetPosition(Vector3f pos) {
        //containers[pos.x][pos.z][pos.y] = null;
    }
    
    public int getId() {
        return id;
    }
    
}
