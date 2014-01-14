/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Platform;

import containing.Platform.Platform.Positie;
import containing.Road.Road;
import containing.Settings;
import containing.Vector3f;
import java.io.Serializable;

public class StoragePlatformOrientation extends Platform implements Serializable {
    
    private final Vector3f entrypoint;
    private final Vector3f exitpoint;
    
    /**
     * Create a subplatform inside StoragePlatform (left and right)
     * @param positie position in port
     * @param road road of subplatform
     * @param entrypoint entrypoint of the road
     * @param exitpoint exitpoint of the road
     */
    public StoragePlatformOrientation(Positie positie, Road road, Vector3f entrypoint, Vector3f exitpoint) {
        super(new Vector3f(110f*Settings.METER , 5.5f, 19f*Settings.METER), positie);
        this.positie = positie;
        this.road = road;
        this.entrypoint = entrypoint;
        this.exitpoint = exitpoint;
    }
    
    /**
     * Return positie of this subplatform (LEFT or RIGHT)
     * @return 
     */
    public Positie getPositie() {
        return positie;
    }
    
    /**
     * Return road of this subplatform
     * @return road
     */
    @Override
    public Road getRoad() {
        return road;
    }
    
    /**
     * Return entrypoint of this subplatform
     * @return list of 3 points
     */
    @Override
    public Vector3f getEntrypoint() {
        return entrypoint;
    }
    
    /**
     * Return exitpoints of this subplatform
     * @return list of 3 points
     */
    @Override
    public Vector3f getExitpoint() {
        return exitpoint;
    }
    
    /**
     * ignore
     */
    @Override
    protected void createCranes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * ignore
     */
    @Override
    protected void createExtVehicleSpots() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
