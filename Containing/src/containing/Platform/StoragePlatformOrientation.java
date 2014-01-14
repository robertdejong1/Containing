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
    
    public StoragePlatformOrientation(Positie positie, Road road, Vector3f entrypoint, Vector3f exitpoint) {
        super(new Vector3f(110f*Settings.METER , 5.5f, 19f*Settings.METER), positie);
        this.positie = positie;
        this.road = road;
        this.entrypoint = entrypoint;
        this.exitpoint = exitpoint;
    }
    
    public Positie getPositie() {
        return positie;
    }
    
    @Override
    public Road getRoad() {
        return road;
    }
    
    @Override
    public Vector3f getEntrypoint() {
        return entrypoint;
    }
    
    @Override
    public Vector3f getExitpoint() {
        return exitpoint;
    }

    @Override
    protected void createCranes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void createExtVehicleSpots() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
