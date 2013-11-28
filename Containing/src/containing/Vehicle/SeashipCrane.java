/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Platform.Platform;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public class SeashipCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    
    //hier SeashipCrane specific variables
    
    public SeashipCrane(Vector3f startPosition, Platform platform){ //variabelen doorgeven aan constructor crane
        super(startPosition, platform);
        id = counter;
        counter++;
    }
    
    public Container unload(){return super.unload();}
    
    public void load(Container container){super.load(container);}
    
    public void reset(){super.reset();}
  
    public void move(int direction){ //-1 = left + 1 = rechts
        int starttime = this.timeCounter;
        while (this.timeCounter < starttime + this.metersToNextAgvSpot * this.maxSpeedUnloaded){}
        this.startPosition.x = this.startPosition.x + direction * this.metersToNextAgvSpot;
    }
    
    @Override
    public int getMaxSpeedUnloaded(){return 4;}
    @Override
    public int getMaxSpeedLoaded(){return 0;}
}
