/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Platform;

import containing.Platform.Platform.Positie;
import containing.Road.Road;

public class StoragePlatformLoadLeft {
    
    private final Positie positie;
    
    private Road road;
    
    public StoragePlatformLoadLeft(Positie positie, Road road) {
        this.positie = positie;
        this.road = road;
    }
    
    public Road getRoad() {
        return road;
    }
    
}
