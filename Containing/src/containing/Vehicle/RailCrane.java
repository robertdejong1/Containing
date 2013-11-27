/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containing.Vehicle;

import containing.Container;
import containing.Vector3f;

/**
 *
 * @author Robert
 */
public class RailCrane extends Crane {
    private static int counter = 0;
    private int id;
    private int currentRow;
    /*
    private final int liftTimeMin = 0;
    private final float liftTimeMax = 3.5f * 60;
    private final float dropTimeMin = 0;
    private final float dropTimeMax = 3.5f * 60;*/
    
    public RailCrane(){this(new Vector3f(0,0,0));} //om te testen zolang posities niet bekend zijn
    
    public RailCrane(Vector3f startPosition){ //variabelen doorgeven aan constructor crane
        super(startPosition);
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
    public int getMaxSpeedUnloaded(){return 3;}
    @Override
    public int getMaxSpeedLoaded(){return 2;}
    
    
}